package com.traffgun.acc.controller;

import com.traffgun.acc.dto.user.ChangePasswordRequest;
import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.exception.UserIsAdminException;
import com.traffgun.acc.mapper.UserMapper;
import com.traffgun.acc.model.Role;
import com.traffgun.acc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public UserResponse getCurrentUser() throws IllegalAccessException {
        User user = userService.getCurrentUser();
        return userMapper.toUserDto(user);
    }

    @GetMapping
    public Page<UserResponse> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Role role,
            @RequestParam(name = "sort_by", defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<User> users = userService.findAll(username, role, sortBy, direction, page, size);
        return users.map(userMapper::toUserDto);
    }

    @PostMapping("/change-password/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) throws IllegalAccessException {
        User user = userService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (user.getRole() == Role.ADMIN) {
            throw new UserIsAdminException();
        }
        userService.changePassword(user, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> changeRole(@PathVariable Long id, Role role) {
        User user = userService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        userService.changeRole(user, role);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (user.getRole() == Role.ADMIN) {
            throw new UserIsAdminException();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
