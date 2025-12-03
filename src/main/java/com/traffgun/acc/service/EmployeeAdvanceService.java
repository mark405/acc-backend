package com.traffgun.acc.service;

import com.traffgun.acc.dto.employee.CreateAdvanceRequest;
import com.traffgun.acc.dto.employee.UpdateAdvanceRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeAdvance;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.repository.EmployeeAdvanceRepository;
import com.traffgun.acc.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeAdvanceService {

    private final EmployeeAdvanceRepository repository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Optional<EmployeeAdvance> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public EmployeeAdvance create(CreateAdvanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(request.getEmployeeId()));

        return repository.save(EmployeeAdvance.builder()
                .employee(employee)
                .date(request.getDate())
                .amount(request.getAmount())
                .build()
        );
    }

    @Transactional
    public EmployeeAdvance update(EmployeeAdvance advance, UpdateAdvanceRequest request) {
        advance.setDate(request.getDate());
        advance.setAmount(request.getAmount());
        return repository.save(advance);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EmployeeAdvance> findAllByDates(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(employeeId));

        ZoneId zone = ZoneId.of("Europe/Kyiv");

        Instant startInstant = startDate.atStartOfDay(zone).toInstant();
        Instant endInstant = endDate.plusDays(1).atStartOfDay(zone).toInstant();

        return repository.findAllByEmployeeAndDateBetween(employee, startInstant, endInstant);
    }
}
