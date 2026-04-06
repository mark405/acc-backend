-- Copy role from users to employees
UPDATE employees e
SET role = u.role
    FROM users u
WHERE e.user_id = u.id and e.role is null;

-- Reset all users to USER except admin
UPDATE users
SET role = 'USER'
WHERE role != 'ADMIN';