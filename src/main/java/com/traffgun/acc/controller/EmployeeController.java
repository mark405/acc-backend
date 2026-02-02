package com.traffgun.acc.controller;

import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.dto.employee.UpdateEmployeeRequest;
import com.traffgun.acc.entity.Category;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.EmployeeMapper;
import com.traffgun.acc.service.EmployeeService;
import com.traffgun.acc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UserService userService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/by_user")
    public EmployeeResponse getEmployeeByUserId() throws IllegalAccessException {
        User user = userService.getCurrentUser();
        Employee employee = employeeService.findByUser(user);
        return employeeMapper.toDto(employee);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return employeeMapper.toDto(employee);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeResponse updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeRequest request) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Employee updatedEmployee = employeeService.update(employee, request);
        return employeeMapper.toDto(updatedEmployee);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<EmployeeResponse> getAllEmployees(
            @RequestParam(name = "name_or_comment", required = false) String nameOrComment,
            @RequestParam(name = "sort_by", defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<Employee> employees = employeeService.findAll(nameOrComment, sortBy, direction, page, size);
        return employees.map(employeeMapper::toDto);
    }
}
