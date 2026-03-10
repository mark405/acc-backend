package com.traffgun.acc.service;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.History;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.repository.EmployeeRepository;
import com.traffgun.acc.repository.HistoryRepository;
import com.traffgun.acc.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public History create(History history) {
        return historyRepository.save(history);
    }

    @Transactional(readOnly = true)
    public Page<History> findAll(String name, HistoryType type, String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        List<Employee> employees = null;
        if (name != null && !name.isBlank()) {
            Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

            spec = spec.and(EmployeeSpecification.hasNameLike(name));
            employees = employeeRepository.findAll(spec);
        }

        if (employees != null && type != null) {
            return historyRepository.findByEmployeeInAndType(employees, type, pageable);
        } else if (employees != null) {
            return historyRepository.findByEmployeeIn(employees, pageable);
        } else if (type != null) {
            return historyRepository.findByType(type, pageable);
        } else {
            return historyRepository.findAll(pageable);
        }
    }
}
