-- 1. add new column
ALTER TABLE histories
    ADD COLUMN employee_id bigint;

-- 2. fill it by matching user_id
UPDATE histories h
SET employee_id = e.id
    FROM employees e
WHERE e.user_id = h.user_id;

DELETE FROM histories h
WHERE h.employee_id IS NULL;

-- 3. make it required
ALTER TABLE histories
    ALTER COLUMN employee_id SET NOT NULL;

-- 4. add FK
ALTER TABLE histories
    ADD CONSTRAINT fk_history_employee
        FOREIGN KEY (employee_id) REFERENCES employees(id);

-- 5. remove old column
ALTER TABLE histories
DROP COLUMN user_id;