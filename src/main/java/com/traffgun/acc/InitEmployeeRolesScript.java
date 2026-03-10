package com.traffgun.acc;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitEmployeeRolesScript {

    private final JdbcTemplate jdbcTemplate;

    public InitEmployeeRolesScript(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void runScript() {
        jdbcTemplate.execute("""
            UPDATE employees e
            SET role = u.role
            FROM users u
            WHERE e.user_id = u.id;
        """);
        // 1. Update Employee.role based on their User.role

        // 2. Set all users to role 'USER', except admin
        jdbcTemplate.execute("""
            UPDATE users
            SET role = 'USER'
            WHERE role != 'ADMIN';
        """);
    }
}