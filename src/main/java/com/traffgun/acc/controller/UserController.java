package com.traffgun.acc.controller;

import com.traffgun.acc.dto.user.ChangePasswordRequest;
import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.exception.UserIsAdminException;
import com.traffgun.acc.mapper.ProjectMapper;
import com.traffgun.acc.mapper.UserMapper;
import com.traffgun.acc.model.UserRole;
import com.traffgun.acc.service.EmployeeService;
import com.traffgun.acc.service.ProjectService;
import com.traffgun.acc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    @GetMapping("/me")
    public UserResponse getCurrentUser() throws IllegalAccessException {
        User user = userService.getCurrentUser();
        return userMapper.toUserDto(user);
    }

    @GetMapping
    public Page<UserResponse> getAllUsers(
            @RequestParam(name = "project_id", required = false) Long projectId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) UserRole role,
            @RequestParam(name = "sort_by", defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<User> users = userService.findAll(projectId, username, role, sortBy, direction, page, size);
        Set<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toUnmodifiableSet());

        List<Employee> employees = employeeService.findAllByUserIds(userIds);

        Set<Long> projectIds = employees.stream()
                .map(Employee::getProjectId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Project> projects = projectService.findAllByIds(projectIds);

        Map<Long, Project> projectsById = projects.stream()
                .collect(Collectors.toMap(Project::getId, Function.identity()));

        Map<Long, List<Project>> projectsByUserId = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getUserId,
                        Collectors.mapping(
                                emp -> projectsById.get(emp.getProjectId()),
                                Collectors.filtering(Objects::nonNull, Collectors.toList())
                        )
                ));

        return users.map(user -> {
            List<Project> userProjects = projectsByUserId.getOrDefault(user.getId(), List.of());
            return userMapper.toUserDto(user, userProjects.stream().map(projectMapper::toDto).toList());
        });
    }

    @PostMapping("/change-password/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        User user = userService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (user.getRole() == UserRole.ADMIN) {
            throw new UserIsAdminException();
        }
        userService.changePassword(user, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws IllegalAccessException {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
