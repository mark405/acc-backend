package com.traffgun.acc.specification;

import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.model.OperationType;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class OperationSpecification {

    public static Specification<Operation> hasBoardId(Long boardId) {
        return (root, query, cb) ->
                boardId == null ? null : cb.equal(root.get("board").get("id"), boardId);
    }

    public static Specification<Operation> hasType(OperationType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("operationType"), type);
    }

    public static Specification<Operation> hasCategoryId(Long categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Operation> hasCommentLike(String comment) {
        return (root, query, cb) ->
                comment == null || comment.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("comment")), "%" + comment.toLowerCase() + "%");
    }

    public static Specification<Operation> hasDateAfter(Instant startDate) {
        return (root, query, cb) ->
                startDate == null ? null : cb.greaterThanOrEqualTo(root.get("date"), startDate);
    }

    public static Specification<Operation> hasDateBefore(Instant endDate) {
        return (root, query, cb) ->
                endDate == null ? null : cb.lessThanOrEqualTo(root.get("date"), endDate);
    }
}
