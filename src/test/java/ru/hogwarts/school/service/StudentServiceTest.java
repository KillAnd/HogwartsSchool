package ru.hogwarts.school.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;
    private long idCounter;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
        studentService.createStudent(new Student(1L, "student", 13));
        studentService.createStudent(new Student(2L, "student1", 14));
        studentService.createStudent(new Student(3L, "student2", 15));
    }
    @Test
    void CheckingCreateStudent() {
        Student student = new Student(4L, "Jonh", 35);

        studentService.createStudent(student);
        Student result = studentService.getStudent(4);

        Assertions.assertEquals(student,result);
    }

    @Test
    void CheckingGetStudent() {
        Student result = studentService.getStudent(1);

        Student checkingStudent = new Student(1L, "student", 13);

        Assertions.assertEquals(result,checkingStudent);
    }

    @Test
    void updateStudent() {
        Student checkingStudent = new Student(1L, "Ivan", 13);

        Student result = studentService.updateStudent(1, checkingStudent);
        Student resultGet = studentService.getStudent(1);

        Assertions.assertEquals(resultGet,checkingStudent);
    }

    @Test
    void deleteStudent() {
        Student deleted = studentService.deleteStudent(2);
        Student studentDeleted = studentService.getStudent(2);

        Assertions.assertNull(studentDeleted);
    }

    @Test
    void filterByAge() {
        Student resultGet = studentService.getStudent(1);

        List<Student> studentsFilter = studentService.filterByAge(13);

        Assertions.assertTrue(studentsFilter.
                stream().
                anyMatch(student -> student.getName().equals("student")));
    }

    @Test
    void allStudent() {
        int sizeExpected = 3;

        int resultSize = studentService.allStudent().size();

        Assertions.assertEquals(sizeExpected,resultSize);
    }
}