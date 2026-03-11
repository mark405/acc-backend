package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.History;
import com.traffgun.acc.model.history.HistoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History> {
    Page<History> findByType(HistoryType type, Pageable pageable);

    Page<History> findByEmployeeInAndType(List<Employee> employees, HistoryType type, Pageable pageable);

    Page<History> findByEmployeeIn(List<Employee> employees, Pageable pageable);
}
