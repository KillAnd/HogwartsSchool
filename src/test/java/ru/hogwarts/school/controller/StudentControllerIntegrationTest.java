package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StudentControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void clearDB() {
        studentRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(studentRepository).isNotNull();
    }

    @Test
    void correctlyCreatedFaculty() {
        //дано
        Student student = new Student("Гарри", 12);

        //когда
        ResponseEntity<Student> studentResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/students"
                ,student
                ,Student.class
        );

        //затем

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertNotNull(actualStudent.getId());
        assertEquals(actualStudent.getName(),student.getName());
        assertEquals(actualStudent.getAge(),student.getAge());
    }

    @Test
    void correctlyUpdateFaculty() {
        Student student = new Student("Гарри", 12);
        student = studentRepository.save(student);

        Student newStudent = new Student("Гарри", 12);

        HttpEntity<Student> entity = new HttpEntity<>(newStudent);
        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/" + student.getId(),
                HttpMethod.PUT,
                entity,
                Student.class
        );

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertEquals(actualStudent.getId(),student.getId());
        assertEquals(actualStudent.getName(),newStudent.getName());
        assertEquals(actualStudent.getAge(),newStudent.getAge());
    }

    @Test
    void correctlyGetFaculty() {
        Student student = new Student("Гарри", 12);
        student = studentRepository.save(student);

        ResponseEntity<Student> studentResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + student.getId(),
                Student.class
        );

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertEquals(actualStudent.getName(),student.getName());
        assertEquals(actualStudent.getAge(),student.getAge());
    }

    @Test
    void correctlyDelete() {
        Student student = new Student("Гарри", 12);
        student = studentRepository.save(student);

        ResponseEntity<Student> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/" + student.getId(),
                HttpMethod.DELETE,
                null,
                Student.class
        );

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Student actualStudent = studentResponseEntity.getBody();
        assertThat(facultyRepository.findById(student.getId())).isNotPresent();
    }

    @Test
    void correctlyFilterFacultyByColor() {
        Student student = new Student("Гарри", 12);
        student = studentRepository.save(student);
        Student student1 = new Student("Гермиона", 13);
        student1 = studentRepository.save(student1);

        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students?age=12",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> students = studentResponseEntity.getBody();
        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals("Гарри", students.get(0).getName());
        assertEquals(12, students.get(0).getAge());
    }

    @Test
    public void correctlyFindByNameOrColor() {
        Student student = new Student("Гарри", 12);
        student = studentRepository.save(student);
        Student student1 = new Student("Гермиона", 13);
        student1 = studentRepository.save(student1);

        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/byAge?fromAge=12&toAge=13",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> students = studentResponseEntity.getBody();
        assertNotNull(students);
        assertEquals(2, students.size());
        assertEquals("Гарри", students.get(0).getName());
        assertEquals("Гермиона", students.get(1).getName());
    }

    @Test
    public void correctlyGetAllFaculty() {
        Student student = new Student("Гарри", 12);
        student = studentRepository.save(student);
        Student student1 = new Student("Гермиона", 13);
        student1 = studentRepository.save(student1);

        ResponseEntity<List<Student>> studentResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/students/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertNotNull(studentResponseEntity);
        assertEquals(studentResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        List<Student> students = studentResponseEntity.getBody();
        assertNotNull(students);
        assertEquals(2, students.size());
    }

    @Test
    public void correctlyGetStudentsByFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);

        Student student1 = new Student("Гарри",12,faculty);
        student1 = studentRepository.save(student1);
        Student student2 = new Student("Гермиона",13,faculty);
        student2 = studentRepository.save(student2);


        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + student2.getId() + "/faculty",
                Faculty.class);

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getName(),faculty.getName());
        assertEquals(actualFaculty.getColor(),faculty.getColor());
    }
}
