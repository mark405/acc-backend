package com.traffgun.acc.service;

import com.traffgun.acc.dto.employee.CreateFinanceRequest;
import com.traffgun.acc.dto.employee.UpdateFinanceRequest;
import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeFinance;
import com.traffgun.acc.entity.History;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.model.history.EmployeeInfoCreatedHistoryBody;
import com.traffgun.acc.model.history.EmployeeInfoDeletedHistoryBody;
import com.traffgun.acc.model.history.EmployeeInfoUpdatedHistoryBody;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.repository.EmployeeFinanceRepository;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.HistoryRepository;
import com.traffgun.acc.repository.ProjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeFinanceService {

    private final EmployeeFinanceRepository repository;
    private final EmployeeRepository employeeRepository;
    private final HistoryRepository historyRepository;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public Optional<EmployeeFinance> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public EmployeeFinance create(CreateFinanceRequest request) throws IllegalAccessException {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(request.getEmployeeId()));
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException(request.getProjectId()));
        var saved = repository.save(EmployeeFinance.builder()
                .employee(employee)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build()
        );

        historyRepository.save(History.builder()
                .employee(employee)
                .type(HistoryType.EMPLOYEE)
                .body(new EmployeeInfoCreatedHistoryBody(employee.getName(), saved.getStartDate(), saved.getEndDate()))
                .project(project)
                .build()
        );

        return saved;
    }

    @Transactional
    public EmployeeFinance update(EmployeeFinance finance, @Valid UpdateFinanceRequest request) throws IllegalAccessException {
        finance.setStartDate(request.getStartDate());
        finance.setEndDate(request.getEndDate());
        var updated = repository.save(finance);

        historyRepository.save(History.builder()
                .employee(finance.getEmployee())
                .type(HistoryType.EMPLOYEE)
                .body(new EmployeeInfoUpdatedHistoryBody(finance.getEmployee().getName(), finance.getStartDate(), finance.getEndDate()))
                .project(finance.getEmployee().getProject())
                .build()
        );

        return updated;
    }

    @Transactional
    public void deleteById(Long id) throws IllegalAccessException {
        EmployeeFinance finance = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        repository.deleteById(id);

        historyRepository.save(History.builder()
                .employee(finance.getEmployee())
                .type(HistoryType.EMPLOYEE)
                .body(new EmployeeInfoDeletedHistoryBody(finance.getEmployee().getName(), finance.getStartDate(), finance.getEndDate()))
                .project(finance.getEmployee().getProject())
                .build()
        );
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
