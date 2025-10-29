package com.traffgun.acc.service;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.repository.EmployeeRepository;
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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Page<Employee> findAll(String nameOrComment, String sortBy, String direction, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (nameOrComment == null || nameOrComment.isBlank()) {
            return employeeRepository.findAll(pageable);
        } else {
            return employeeRepository.findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(nameOrComment, nameOrComment, pageable);
        }
    }
}
