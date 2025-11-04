package com.traffgun.acc.specification;

import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.Role;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasUsernameLike(String username) {
        return (root, query, cb) ->
                (username == null || username.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }
}
