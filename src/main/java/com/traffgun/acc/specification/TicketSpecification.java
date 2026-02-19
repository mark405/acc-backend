package com.traffgun.acc.specification;

import com.traffgun.acc.entity.Ticket;
import com.traffgun.acc.model.TicketStatus;
import com.traffgun.acc.model.TicketType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TicketSpecification {
    public static Specification<Ticket> hasTypes(List<TicketType> types) {
        return (root, query, cb) -> {
            if (types == null || types.isEmpty()) {
                return null;
            }
            return root.get("type").in(types);
        };
    }


    public static Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }

            if (status == TicketStatus.OPENED) {
                return root.get("status").in(TicketStatus.OPENED, TicketStatus.IN_PROGRESS);
            }

            if (status == TicketStatus.CLOSED) {
                return cb.equal(root.get("status"), TicketStatus.CLOSED);
            }

            return null;
        };
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
