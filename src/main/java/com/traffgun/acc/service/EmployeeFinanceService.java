package com.traffgun.acc.service;

import com.traffgun.acc.dto.employee.CreateFinanceRequest;
import com.traffgun.acc.dto.employee.UpdateFinanceRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeFinance;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.repository.EmployeeFinanceRepository;
import com.traffgun.acc.repository.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeFinanceService {

    private final EmployeeFinanceRepository repository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Optional<EmployeeFinance> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public EmployeeFinance create(CreateFinanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(request.getEmployeeId()));
        return repository.save(EmployeeFinance.builder()
                .employee(employee)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .incomeQFD(request.getIncomeQFD())
                .paidRef(request.getPaidRef())
                .percentQFD(request.getPercentQFD())
                .build()
        );
    }

    @Transactional
    public EmployeeFinance update(EmployeeFinance finance, @Valid UpdateFinanceRequest request) {
        finance.setStartDate(request.getStartDate());
        finance.setEndDate(request.getEndDate());
        finance.setIncomeQFD(request.getIncomeQFD());
        finance.setPaidRef(request.getPaidRef());
        finance.setPercentQFD(request.getPercentQFD());
        return repository.save(finance);
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeFinance> findAll(Long employeeId, String sortBy, String direction, int page, int size) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(employeeId));

        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAllByEmployee(employee, pageable);
    }
}
