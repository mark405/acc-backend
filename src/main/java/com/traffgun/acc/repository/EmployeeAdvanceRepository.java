package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeAdvance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EmployeeAdvanceRepository extends JpaRepository<EmployeeAdvance, Long> {
    List<EmployeeAdvance> findAllByEmployeeAndDateBetween(Employee employee, Instant startInstant, Instant endInstant);
}
