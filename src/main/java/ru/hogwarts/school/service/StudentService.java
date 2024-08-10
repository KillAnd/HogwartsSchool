package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        return studentRepository.getReferenceById(id);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }
    public Long deleteStudent(long id) {
        studentRepository.deleteById(id);
        return id;
    }

    public List<Student> filterByAge(int age) {
        return studentRepository.findAll()
                .stream().
                filter(fil -> fil.getAge() == age).
                collect(Collectors.toList());
    }

    public List<Student> allStudent() {
        return studentRepository.findAll();
    }

}
