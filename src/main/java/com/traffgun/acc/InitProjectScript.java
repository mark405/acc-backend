package com.traffgun.acc;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitProjectScript {

    private final JdbcTemplate jdbcTemplate;

    public InitProjectScript(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void runScript() {
        jdbcTemplate.execute("""
            DO $$
            DECLARE
                new_project_id bigint;
            BEGIN
                IF NOT EXISTS (SELECT 1 FROM projects WHERE id = 1) THEN
                    SELECT id INTO new_project_id
                    FROM users
                    WHERE username = 'admin'
                    LIMIT 1;

                    INSERT INTO projects (name, created_by)
                    VALUES ('test', new_project_id)
                    RETURNING id INTO new_project_id;

                    UPDATE boards          SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE categories      SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE employees       SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE employee_advance SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE employee_finance SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE histories       SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE operations      SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE tickets         SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE ticket_comments SET project_id = new_project_id WHERE project_id IS NULL;
                    UPDATE ticket_files    SET project_id = new_project_id WHERE project_id IS NULL;
                END IF;
            END
            $$;
        """);
    }
}