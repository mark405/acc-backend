package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByNameContainingIgnoreCaseOrCommentContainingIgnoreCase(String name, String comment, Pageable pageable);
}
