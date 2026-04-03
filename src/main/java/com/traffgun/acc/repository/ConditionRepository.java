package com.traffgun.acc.repository;

import com.traffgun.acc.entity.EmployeeCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConditionRepository extends JpaRepository<EmployeeCondition, Long> {
    Optional<EmployeeCondition> findByEmployeeId(Long employeeId);
}
