package com.traffgun.acc.repository;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @EntityGraph(value = "Employee.user")
    Optional<Employee> findByUserAndProjectAndActiveIsTrue(User user, Project project);

    @EntityGraph(value = "Employee.user")
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    Optional<Employee> findByNameAndActiveIsTrue(String name);

    @EntityGraph(value = "Employee.user")
    Optional<Employee> findByIdAndActiveIsTrue(Long id);

    @EntityGraph(value = "Employee.user")
    List<Employee> findAllByUserAndActiveIsTrue(User user);
}
