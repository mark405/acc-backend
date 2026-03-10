package com.traffgun.acc.controller;

import com.traffgun.acc.dto.employee.CreateEmployeeRequest;
import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.dto.employee.UpdateEmployeeRequest;
import com.traffgun.acc.dto.user.ChangeRoleRequest;
import com.traffgun.acc.entity.Category;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.EmployeeMapper;
import com.traffgun.acc.service.EmployeeService;
import com.traffgun.acc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/by_user/{project_id}")
    public EmployeeResponse getEmployeeByUserId(@PathVariable("project_id") Long projectId) throws IllegalAccessException {
        Employee employee = employeeService.findByUser(projectId);
        return employeeMapper.toDto(employee);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return employeeMapper.toDto(employee);
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeRequest request) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Employee updatedEmployee = employeeService.update(employee, request);
        return employeeMapper.toDto(updatedEmployee);
    }

    @PostMapping()
    public EmployeeResponse createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee createdEmployee = employeeService.create(request);
        return employeeMapper.toDto(createdEmployee);
    }

    @GetMapping
    public Page<EmployeeResponse> getAllEmployees(
            @RequestParam(name = "project_id") Long projectId,
            @RequestParam(name = "name_or_comment", required = false) String nameOrComment,
            @RequestParam(name = "sort_by", defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<Employee> employees = employeeService.findAll(projectId, nameOrComment, sortBy, direction, page, size);
        return employees.map(employeeMapper::toDto);
    }

    @PutMapping("/change-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request) throws IllegalAccessException {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        employeeService.changeRole(employee, request.getRole());
        return ResponseEntity.noContent().build();
    }
}
