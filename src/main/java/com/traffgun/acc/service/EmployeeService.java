package com.traffgun.acc.service;

import com.traffgun.acc.dto.employee.ColumnResponse;
import com.traffgun.acc.dto.employee.CreateEmployeeRequest;
import com.traffgun.acc.dto.employee.UpdateEmployeeRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeColumn;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.EmployeeRole;
import com.traffgun.acc.repository.ColumnRepository;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.ProjectRepository;
import com.traffgun.acc.repository.ValueRepository;
import com.traffgun.acc.specification.EmployeeSpecification;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final UserService userService;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ColumnRepository columnRepository;
    private final ValueRepository valueRepository;

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findByIdAndActiveIsTrue(id);
    }

    @Transactional(readOnly = true)
    public Page<Employee> findAll(Long projectId, EmployeeRole role, String nameOrComment, String sortBy, String direction, int page, int size) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

        spec = spec
                .and(EmployeeSpecification.hasRole(role))
                .and(EmployeeSpecification.hasProject(projectId))
                .and(EmployeeSpecification.hasNameOrComment(nameOrComment))
                .and(EmployeeSpecification.hasActiveTrue());

        return employeeRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findByUser(Long projectId) throws IllegalAccessException {
        User user = userService.getCurrentUser();
        return employeeRepository.findByUserAndProjectAndActiveIsTrue(user, projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(projectId)));
    }

    @Transactional
    public Employee update(Employee employee, UpdateEmployeeRequest request) {
        employee.setName(request.getName());
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee create(CreateEmployeeRequest request) {
        Employee employee = Employee.builder()
                .name(request.getName())
                .project(projectRepository.findById(request.getProjectId()).orElseThrow(() -> new EntityNotFoundException(request.getProjectId())))
                .user(userService.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException(request.getUserId())))
                .role(EmployeeRole.MANAGER)
                .build();

        return employeeRepository.save(employee);
    }

    @Transactional
    public void changeRole(Employee employee, EmployeeRole role) {
        employee.setRole(role);
        employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAllByIds(@NotEmpty List<Long> assignedTo) {
        return employeeRepository.findAllById(assignedTo);
    }

    @Transactional
    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        employee.setActive(false);
        employee.setProject(null);
        employeeRepository.save(employee);
    }

    public List<Employee> findAllByUserIds(Set<Long> userIds) {
        return employeeRepository.findAllByUserIdIn(userIds);
    }

    @Transactional
    public EmployeeColumn addColumn(Long id, String name) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return columnRepository.save(EmployeeColumn.builder().employee(employee).name(name).build());
    }

    @Transactional
    public EmployeeColumn editColumn(Long id, String name) {
        EmployeeColumn column = columnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        column.setName(name);
        return columnRepository.save(column);
    }

    @Transactional
    public void deleteColumn(Long id) {
        EmployeeColumn column = columnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        valueRepository.deleteByEmployeeColumnId(id);
        columnRepository.delete(column);
    }
}
