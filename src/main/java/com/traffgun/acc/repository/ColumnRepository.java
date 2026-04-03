package com.traffgun.acc.repository;

import com.traffgun.acc.entity.EmployeeColumn;
import com.traffgun.acc.entity.EmployeeCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<EmployeeColumn, Long> {
}
