package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FacultyControllerIntegrationTest {
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
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyRepository).isNotNull();
    }

    @Test
    void correctlyCreatedFaculty() {
        //дано
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");

        //когда
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty"
                        ,faculty
                ,Faculty.class
        );

        //затем

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertNotNull(actualFaculty.getId());
        assertEquals(actualFaculty.getName(),faculty.getName());
        assertEquals(actualFaculty.getColor(),faculty.getColor());
    }

    @Test
    void correctlyUpdateFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);

        Faculty newFaculty = new Faculty("Слизерин", "Зеленый");

        HttpEntity<Faculty> entity = new HttpEntity<>(newFaculty);
        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                HttpMethod.PUT,
                entity,
                Faculty.class
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getId(),faculty.getId());
        assertEquals(actualFaculty.getName(),newFaculty.getName());
        assertEquals(actualFaculty.getColor(),newFaculty.getColor());
    }

    @Test
    void correctlyGetFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                Faculty.class
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertEquals(actualFaculty.getName(),faculty.getName());
        assertEquals(actualFaculty.getColor(),faculty.getColor());
    }

    @Test
    void correctlyDelete() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);

        ResponseEntity<Faculty> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        Faculty actualFaculty = facultyResponseEntity.getBody();
        assertThat(facultyRepository.findById(faculty.getId())).isNotPresent();
    }

    @Test
    void correctlyFilterFacultyByColor() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);
        Faculty faculty1 = new Faculty("Слизерин", "Зеленый");
        faculty1 = facultyRepository.save(faculty1);

        ResponseEntity<List<Faculty>> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?color=Оранжевый",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Faculty> faculties = facultyResponseEntity.getBody();
        assertNotNull(faculties);
        assertEquals(1, faculties.size());
        assertEquals("Гриффиндор", faculties.get(0).getName());
        assertEquals("Оранжевый", faculties.get(0).getColor());
    }

    @Test
    public void correctlyFindByNameOrColor() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);
        Faculty faculty1 = new Faculty("Слизерин", "Зеленый");
        faculty1 = facultyRepository.save(faculty1);

        ResponseEntity<List<Faculty>> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/nameOrColor?name=Гриффиндор&color=Оранжевый",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {});

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Faculty> faculties = facultyResponseEntity.getBody();
        assertNotNull(faculties);
        assertEquals(1, faculties.size());
        assertEquals("Гриффиндор", faculties.get(0).getName());
        assertEquals("Оранжевый", faculties.get(0).getColor());
    }

    @Test
    public void correctlyGetAllFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);
        Faculty faculty1 = new Faculty("Слизерин", "Зеленый");
        faculty1 = facultyRepository.save(faculty1);

        ResponseEntity<List<Faculty>> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {});

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        List<Faculty> faculties = facultyResponseEntity.getBody();
        assertNotNull(faculties);
        assertEquals(2, faculties.size());
    }

    @Test
    public void correctlyGetStudentsByFaculty() {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");
        faculty = facultyRepository.save(faculty);

        Student student = new Student("Гарри",12,faculty);
        student = studentRepository.save(student);
        Student student2 = new Student("Гермиона",13,faculty);
        student2 = studentRepository.save(student2);

        ResponseEntity<List<Student>> facultyResponseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/1/studentsByFaculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertNotNull(facultyResponseEntity);
        assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));

        List<Student> students = facultyResponseEntity.getBody();
        assertNotNull(students);
        assertEquals(2, students.size());

        assertThat(students)
                .extracting(Student::getName)
                .containsExactlyInAnyOrder("Гарри", "Гермиона");
    }
}