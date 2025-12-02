package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.EmployeeFinance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeFinanceRepository extends JpaRepository<EmployeeFinance, Long> {

    Page<EmployeeFinance> findAllByEmployee(Employee employee, Pageable pageable);
}
