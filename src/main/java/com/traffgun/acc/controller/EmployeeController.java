package com.traffgun.acc.controller;

import com.traffgun.acc.dto.employee.EmployeeResponse;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.EmployeeMapper;
import com.traffgun.acc.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return employeeMapper.toDto(employee);
    }

    @GetMapping
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
