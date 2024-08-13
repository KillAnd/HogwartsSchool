package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

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
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id).map(student1 -> {
            student1.setName(student.getName());
            student1.setAge(student.getAge());
            studentRepository.save(student1);
            return student1;
        }).orElse(null);
    }
    public Student deleteStudent(long id) {
        return studentRepository.findById(id).map(student -> {
            studentRepository.deleteById(id);
            return student;
        }).orElse(null);
    }

    public List<Student> filterByAge(int age) {
        return studentRepository.filterByAge(age);
    }

    public List<Student> allStudent() {
        return studentRepository.findAll();
    }

}
