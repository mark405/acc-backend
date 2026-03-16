DO $$
DECLARE
constraint_name text;
BEGIN
SELECT con.conname
INTO constraint_name
FROM pg_constraint con
         JOIN pg_class rel ON rel.oid = con.conrelid
         JOIN pg_attribute att ON att.attrelid = rel.oid
WHERE rel.relname = 'employees'
  AND att.attname = 'user_id'
  AND con.contype = 'u'
  AND att.attnum = ANY(con.conkey);

IF constraint_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE employees DROP CONSTRAINT %I', constraint_name);
END IF;
END $$;