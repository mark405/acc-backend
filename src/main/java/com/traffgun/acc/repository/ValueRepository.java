package com.traffgun.acc.repository;

import com.traffgun.acc.entity.EmployeeColumn;
import com.traffgun.acc.entity.EmployeeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueRepository extends JpaRepository<EmployeeValue, Long> {
    void deleteByEmployeeColumnId(Long employeeColumnId);
}
