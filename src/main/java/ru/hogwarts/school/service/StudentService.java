package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Map<Long, Student> studentList = new HashMap<>();
    private long idCounter = 1L;

    public Student createStudent(Student student) {
        studentList.put(student.getId(), student);
        student.setId(idCounter++);
        return student;
    }

    public Student getStudent(long id) {
        return studentList.get(id);
    }

    public Student updateStudent(long id, Student student) {
        studentList.put(id, student);
        return student;
    }
    public Student deleteStudent(long id) {
        return studentList.remove(id);
    }

    public List<Student> filterByAge(int age) {
        return studentList.values()
                .stream().
                filter(fil -> fil.getAge() == age).
                collect(Collectors.toList());
    }

    public Map<Long, Student> allStudent() {
        return studentList;
    }

}
