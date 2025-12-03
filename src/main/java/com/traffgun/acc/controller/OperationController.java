package com.traffgun.acc.controller;

import com.traffgun.acc.dto.operation.CreateOperationRequest;
import com.traffgun.acc.dto.operation.OperationFilter;
import com.traffgun.acc.dto.operation.OperationResponse;
import com.traffgun.acc.dto.operation.UpdateOperationRequest;
import com.traffgun.acc.entity.Category;
import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.OperationMapper;
import com.traffgun.acc.service.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;
    private final OperationMapper operationMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OperationResponse getOperationById(@PathVariable Long id) {
        Operation operation = operationService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return operationMapper.toDto(operation);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OperationResponse> getAllOperations(@Valid @RequestBody OperationFilter filter) {
        return operationService.findAll(filter).map(operationMapper::toDto);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public OperationResponse createOperation(@RequestBody @Valid CreateOperationRequest request) throws IllegalAccessException {
        return operationMapper.toDto(operationService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OperationResponse updateOperation(@PathVariable("id") Long id, @RequestBody @Valid UpdateOperationRequest request) throws IllegalAccessException {
        Operation operation = operationService.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        Operation updatedOperation = operationService.update(operation, request);
        return operationMapper.toDto(updatedOperation);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOperation(@PathVariable("id") Long id) throws IllegalAccessException {
        operationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
