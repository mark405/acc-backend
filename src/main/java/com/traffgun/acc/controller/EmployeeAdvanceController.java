package com.traffgun.acc.controller;

import com.traffgun.acc.dto.employee.CreateAdvanceRequest;
import com.traffgun.acc.dto.employee.EmployeeAdvanceResponse;
import com.traffgun.acc.dto.employee.UpdateAdvanceRequest;
import com.traffgun.acc.entity.EmployeeAdvance;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.EmployeeAdvanceMapper;
import com.traffgun.acc.service.EmployeeAdvanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee-advances")
@RequiredArgsConstructor
public class EmployeeAdvanceController {

    private final EmployeeAdvanceService service;
    private final EmployeeAdvanceMapper mapper;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeAdvanceResponse createAdvance(@RequestBody @Valid CreateAdvanceRequest request) throws IllegalAccessException {
        EmployeeAdvance advance = service.create(request);
        return mapper.toDto(advance);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeAdvanceResponse updateAdvance(@PathVariable("id") Long id, @RequestBody @Valid UpdateAdvanceRequest request) {
        EmployeeAdvance advance = service.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        EmployeeAdvance updatedAdvance = service.update(advance, request);
        return mapper.toDto(updatedAdvance);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAdvance(@PathVariable("id") Long id) throws IllegalAccessException {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
