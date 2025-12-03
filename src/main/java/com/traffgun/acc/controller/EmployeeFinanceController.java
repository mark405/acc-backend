package com.traffgun.acc.controller;

import com.traffgun.acc.dto.employee.CreateFinanceRequest;
import com.traffgun.acc.dto.employee.EmployeeAdvanceResponse;
import com.traffgun.acc.dto.employee.EmployeeFinanceResponse;
import com.traffgun.acc.dto.employee.UpdateFinanceRequest;
import com.traffgun.acc.entity.EmployeeAdvance;
import com.traffgun.acc.entity.EmployeeFinance;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.mapper.EmployeeAdvanceMapper;
import com.traffgun.acc.mapper.EmployeeFinanceMapper;
import com.traffgun.acc.service.EmployeeAdvanceService;
import com.traffgun.acc.service.EmployeeFinanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee-finances")
@RequiredArgsConstructor
public class EmployeeFinanceController {

    private final EmployeeFinanceService service;
    private final EmployeeAdvanceService advanceService;
    private final EmployeeFinanceMapper mapper;
    private final EmployeeAdvanceMapper advanceMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeFinanceResponse createFinance(@RequestBody @Valid CreateFinanceRequest request) {
        EmployeeFinance finance = service.create(request);
        return mapper.toDto(finance, Collections.emptyList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmployeeFinanceResponse updateFinance(@PathVariable("id") Long id, @RequestBody @Valid UpdateFinanceRequest request) {
        EmployeeFinance finance = service.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        EmployeeFinance updatedFinance = service.update(finance, request);
        return mapper.toDto(updatedFinance, Collections.emptyList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteFinance(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<EmployeeFinanceResponse> getAllFinances(@RequestParam("employeeId") Long employeeId,
                                                        @RequestParam(required = false, value = "sort_by") String sortBy,
                                                        @RequestParam(required = false, value = "direction") String direction,
                                                        @RequestParam(required = false, value = "page") int page,
                                                        @RequestParam(required = false, value = "size") int size) {
        Page<EmployeeFinance> finances = service.findAll(employeeId, sortBy, direction, page, size);

        if (!finances.hasContent()) {
            return finances.map(f -> new EmployeeFinanceResponse(
                    f.getId(),
                    f.getStartDate(),
                    f.getEndDate(),
                    f.getIncomeQFD(),
                    f.getPaidRef(),
                    f.getPercentQFD(),
                    Collections.emptyList()
            ));
        }

        LocalDate minStartDate = finances.stream()
                .map(EmployeeFinance::getStartDate)
                .min(LocalDate::compareTo)
                .orElseThrow();
        LocalDate maxEndDate = finances.stream()
                .map(EmployeeFinance::getEndDate)
                .max(LocalDate::compareTo)
                .orElseThrow();

        List<EmployeeAdvance> allAdvances = advanceService.findAllByDates(employeeId, minStartDate, maxEndDate);

        ZoneId zone = ZoneId.of("Europe/Kyiv");

        Map<Long, List<EmployeeAdvanceResponse>> advancesByFinanceId = finances.stream()
                .collect(Collectors.toMap(
                        EmployeeFinance::getId,
                        f -> allAdvances.stream()
                                .filter(a -> !a.getDate().isBefore(f.getStartDate().atStartOfDay(zone).toInstant())
                                        && a.getDate().isBefore(f.getEndDate().plusDays(1).atStartOfDay(zone).toInstant()))
                                .map(advanceMapper::toDto)
                                .toList()
                ));

        return finances.map(f -> new EmployeeFinanceResponse(
                        f.getId(),
                        f.getStartDate(),
                        f.getEndDate(),
                        f.getIncomeQFD(),
                        f.getPaidRef(),
                        f.getPercentQFD(),
                        advancesByFinanceId.getOrDefault(f.getId(), Collections.emptyList())
                ));
    }
}
