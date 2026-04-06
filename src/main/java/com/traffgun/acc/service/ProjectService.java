package com.traffgun.acc.service;

import com.traffgun.acc.dto.project.CreateProjectRequest;
import com.traffgun.acc.dto.project.UpdateProjectRequest;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.EmployeeRole;
import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import com.traffgun.acc.model.UserRole;
import com.traffgun.acc.repository.BoardRepository;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<Project> findAll() throws IllegalAccessException {
        User user = userService.getCurrentUser();
        if (user.getRole() == UserRole.ADMIN) {
            return projectRepository.findAll();
        }

        List<Employee> employees = employeeRepository.findAllByUserAndActiveIsTrue(user);

        return projectRepository.findAllById(employees.stream().map(it -> it.getProject().getId()).toList());
    }

    @Transactional
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project create(CreateProjectRequest request) throws IllegalAccessException {
        User user = userService.getCurrentUser();
        Project project = projectRepository.save(Project.builder()
                .name(request.getName())
                .createdBy(user)
                .build()
        );

        employeeRepository.save(Employee.builder()
                .name(user.getUsername())
                .project(project)
                .role(EmployeeRole.ADMIN)
                .active(true)
                .user(user)
                .build()
        );

        boardRepository.save(Board.builder().name("Головна").levelType(LevelType.MAIN).operationType(OperationType.EXPENSE).project(project).build());
        boardRepository.save(Board.builder().name("Головна").levelType(LevelType.MAIN).operationType(OperationType.INCOME).project(project).build());

        return project;
    }

    @Transactional
    public Project update(Project project, UpdateProjectRequest request) {
        project.setName(request.getName());
        return projectRepository.save(project);
    }

    public List<Project> findAllByIds(Set<Long> projectIds) {
        return projectRepository.findAllById(projectIds);
    }
}
