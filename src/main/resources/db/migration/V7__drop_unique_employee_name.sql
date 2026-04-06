DO $$
DECLARE
constraint_name text;
BEGIN
SELECT conname INTO constraint_name
FROM pg_constraint
         JOIN pg_class ON pg_constraint.conrelid = pg_class.oid
         JOIN pg_attribute ON pg_attribute.attrelid = pg_class.oid
WHERE pg_class.relname = 'employees'
  AND pg_attribute.attname = 'name'
  AND pg_constraint.contype = 'u'
  AND pg_attribute.attnum = ANY(pg_constraint.conkey);

IF constraint_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE employees DROP CONSTRAINT %I', constraint_name);
END IF;
END $$;