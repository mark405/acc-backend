package com.traffgun.acc.service;

import com.traffgun.acc.dto.project.CreateProjectRequest;
import com.traffgun.acc.dto.project.UpdateProjectRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.UserRole;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<Project> findAll() throws IllegalAccessException {
        User user = userService.getCurrentUser();
        if (user.getRole() == UserRole.ADMIN) {
            return projectRepository.findAll();
        }

        List<Employee> employees = employeeRepository.findAllByUser(user);

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
        return projectRepository.save(Project.builder()
                .name(request.getName())
                .createdBy(userService.getCurrentUser())
                .build()
        );
    }

    @Transactional
    public Project update(Project project, UpdateProjectRequest request) {
        project.setName(request.getName());
        return projectRepository.save(project);
    }
}
