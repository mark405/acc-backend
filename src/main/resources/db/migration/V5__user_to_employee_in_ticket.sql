-- 1. Добавляем новые колонки employee_id (если хочешь переводить на Employee)
ALTER TABLE tickets
    ADD COLUMN created_by_employee_id bigint;

ALTER TABLE tickets
    ADD COLUMN operated_by_employee_id bigint;

-- 2. Заполняем по user_id
UPDATE tickets t
SET created_by_employee_id = e.id
    FROM employees e
WHERE t.created_by = e.user_id;

UPDATE tickets t
SET operated_by_employee_id = e.id
    FROM employees e
WHERE t.operated_by = e.user_id;

-- 3. Делаем NOT NULL для обязательных полей
ALTER TABLE tickets
    ALTER COLUMN created_by_employee_id SET NOT NULL;

-- 4. Добавляем FK
ALTER TABLE tickets
    ADD CONSTRAINT fk_ticket_created_by_employee
        FOREIGN KEY (created_by_employee_id) REFERENCES employees(id);

ALTER TABLE tickets
    ADD CONSTRAINT fk_ticket_operated_by_employee
        FOREIGN KEY (operated_by_employee_id) REFERENCES employees(id);

-- 5. Удаляем старые user колонки
ALTER TABLE tickets
DROP COLUMN created_by;

ALTER TABLE tickets
DROP COLUMN operated_by;

ALTER TABLE tickets
    RENAME COLUMN created_by_employee_id TO created_by;

ALTER TABLE tickets
    RENAME COLUMN operated_by_employee_id TO operated_by;

-- 1. add new column
ALTER TABLE ticket_assignments
    ADD COLUMN employee_id bigint;

-- 2. fill it by matching user_id
UPDATE ticket_assignments ta
SET employee_id = e.id
    FROM employees e
WHERE e.user_id = ta.user_id;

-- 3. remove rows that couldn't be matched
DELETE FROM ticket_assignments
WHERE employee_id IS NULL;

-- 4. make it NOT NULL
ALTER TABLE ticket_assignments
    ALTER COLUMN employee_id SET NOT NULL;

-- 5. add FK
ALTER TABLE ticket_assignments
    ADD CONSTRAINT fk_ticket_assignment_employee
        FOREIGN KEY (employee_id) REFERENCES employees(id);

-- 6. remove old column
ALTER TABLE ticket_assignments
DROP COLUMN user_id;