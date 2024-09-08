-- liquibase formatted sql

-- changeset student:1
CREATE INDEX student_search_name ON students (name);

-- changeset student:2
CREATE INDEX faculty_search_name_and_color ON faculty (name,color);