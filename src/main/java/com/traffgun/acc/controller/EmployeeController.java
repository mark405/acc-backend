package com.traffgun.acc.controller;

import com.traffgun.acc.dto.employee.*;
import com.traffgun.acc.dto.user.ChangeRoleRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.EmployeeMapper;
import com.traffgun.acc.model.EmployeeRole;
import com.traffgun.acc.service.ConditionService;
import com.traffgun.acc.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ConditionService conditionService;
    private final EmployeeMapper employeeMapper;

    @GetMapping("/by_user/{project_id}")
    public EmployeeResponse getEmployeeByUserId(@PathVariable("project_id") Long projectId) throws IllegalAccessException {
        Employee employee = employeeService.findByUser(projectId).orElseThrow(EntityNotFoundException::new);
        return employeeMapper.toDto(employee);
    }

    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return employeeMapper.toDto(employee);
    }

    @GetMapping("/{id}/conditions")
    public String getConditions(@PathVariable Long id) {
        return conditionService.findByEmployeeId(id);
    }

    @PostMapping("/{id}/conditions")
    public String addConditions(@PathVariable("id") Long id, @RequestBody AddConditionsRequest request) {
        return conditionService.add(id, request.getText()).getText();
    }

    @PostMapping("/{id}/columns")
    public void addColumn(@PathVariable("id") Long id, @RequestBody AddColumnRequest request) {
       employeeService.addColumn(id, request.getName());
    }

    @PutMapping("/{id}/columns")
    public void editColumn(@PathVariable("id") Long id, @RequestBody EditColumnRequest request) {
        employeeService.editColumn(id, request.getName());
    }

    @DeleteMapping("/{id}/columns")
    public void deleteColumn(@PathVariable("id") Long id) {
        employeeService.deleteColumn(id);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<EmployeeResponse> getAllEmployees(
            @RequestParam(name = "project_id") Long projectId,
            @RequestParam(name = "name_or_comment", required = false) String nameOrComment,
            @RequestParam(name = "sort_by", defaultValue = "name") String sortBy,
            @RequestParam(name = "role", required = false) EmployeeRole role,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Page<Employee> employees = employeeService.findAll(projectId, role, nameOrComment, sortBy, direction, page, size);
        return employees.map(employeeMapper::toDto);
    }

    @PutMapping("/change-role/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request) {
        Employee employee = employeeService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        employeeService.changeRole(employee, request.getRole());
        return ResponseEntity.noContent().build();
    }
}
