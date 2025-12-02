package com.traffgun.acc.repository;

import com.traffgun.acc.entity.EmployeeAdvance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EmployeeAdvanceRepository extends JpaRepository<EmployeeAdvance, Long> {

    List<EmployeeAdvance> findAllByDateBetween(Instant dateAfter, Instant dateBefore);
}
