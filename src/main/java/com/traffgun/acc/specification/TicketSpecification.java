package com.traffgun.acc.specification;

import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import org.springframework.data.jpa.domain.Specification;

public class TicketSpecification {

    public static Specification<Ticket> hasType(TicketType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Ticket> hasCreatedBy(Long createdBy) {
        return (root, query, cb) ->
                createdBy == null ? null : cb.equal(root.get("createdBy").get("id"), createdBy);
    }

    public static Specification<Ticket> hasAssignedTo(Long assignedTo) {
        return (root, query, cb) -> {
            if (assignedTo == null) return null;

            var join = root.join("assignedTo");

            return cb.equal(join.get("id"), assignedTo);
        };
    }
}
