ALTER TABLE students ADD CONSTRAINT age CHECK (age > 0);
ALTER TABLE students ADD CONSTRAINT name UNIQUE (name);
ALTER TABLE faculty 
ADD CONSTRAINT name_faculty_unique UNIQUE (name, color);
ALTER TABLE students ALTER COLUMN age SET DEFAULT 20;