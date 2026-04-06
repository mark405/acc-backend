package com.traffgun.acc.specification;

import com.traffgun.acc.entity.Employee;
import com.traffgun.acc.entity.History;
import com.traffgun.acc.model.history.HistoryType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;

public class HistorySpecification {
    public static Specification<History> hasType(HistoryType type) {
        if (type == null) return null;
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<History> hasName(String name) {
        if (name == null || name.isBlank()) return null;
        return (root, query, cb) -> {
            Join<History, Employee> employeeJoin = root.join("employee", JoinType.INNER);
            return cb.like(cb.lower(employeeJoin.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<History> hasProjectId(@NotNull Long projectId) {
        return (root, query, cb) ->
                cb.equal(root.get("project").get("id"), projectId);
    }
}