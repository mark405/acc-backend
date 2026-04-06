package com.traffgun.acc.specification;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.model.EmployeeRole;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {
    public static Specification<Employee> hasProject(Long projectId) {
        return (root, query, cb) ->
                projectId == null
                        ? null
                        : cb.equal(root.get("project").get("id"), projectId);
    }

    public static Specification<Employee> hasNameLike(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Employee> hasNameOrComment(String value) {
        return (root, query, cb) -> {
            if (value == null || value.isBlank()) {
                return null;
            }

            String pattern = "%" + value.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("comment")), pattern)
            );
        };
    }

    public static Specification<Employee> hasRole(EmployeeRole role) {
        return (root, query, cb) ->
                role == null
                        ? null
                        : cb.equal(root.get("role"), role);
    }

    public static Specification<Employee> hasActiveTrue() {
        return (root, query, cb) ->
                cb.equal(root.get("active"), true);
    }
}
