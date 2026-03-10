package com.traffgun.acc.service;

import com.traffgun.acc.dto.employee.CreateEmployeeRequest;
import com.traffgun.acc.dto.employee.UpdateEmployeeRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.EmployeeRole;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.ProjectRepository;
import com.traffgun.acc.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Employee> findAll(Long projectId, String nameOrComment, String sortBy, String direction, int page, int size) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(EmployeeSpecification.hasProject(projectId))
                .and(EmployeeSpecification.hasNameOrComment(nameOrComment));

        return employeeRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Employee findByUser(Long projectId) throws IllegalAccessException {
        User user = userService.getCurrentUser();
        return employeeRepository.findByUserAndProject(user, projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(projectId)));
    }

    @Transactional
    public Employee update(Employee employee, UpdateEmployeeRequest request) {
        employee.setQfd(request.getQfd());
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee create(CreateEmployeeRequest request) {
        Employee employee = Employee.builder()
                .name(request.getName())
                .qfd(request.getQfd())
                .project(projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException(request.getProjectId())))
                .build();

        return employeeRepository.save(employee);
    }

    @Transactional
    public void changeRole(Employee employee, EmployeeRole role) throws IllegalAccessException {
        employee.setRole(role);
        employeeRepository.save(employee);
    }
}
