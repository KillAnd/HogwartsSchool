SELECT students.name, students.age, faculty.name, faculty.color
FROM students
FULL JOIN faculty ON students.faculty_id = faculty.id;
SELECT students.name, students.age, avatar.data, avatar.file_path, 
		avatar.file_size,
		avatar.media_type
FROM avatar
INNER JOIN students ON avatar.student_id = students.id;