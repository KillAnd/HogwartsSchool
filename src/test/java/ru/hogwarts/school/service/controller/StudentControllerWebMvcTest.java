package ru.hogwarts.school.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {
    @MockBean
    private StudentService studentService;
    @MockBean
    private AvatarService avatarService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateStudent() throws Exception {
        long studentId = 1L;
        Student student = new Student("Гарри",12);
        Student savedStudent = new Student("Гарри",12);
        when(studentService.createStudent(student)).thenReturn(savedStudent);
        savedStudent.setId(studentId);

        ResultActions perform = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedStudent.getId()))
                .andExpect(jsonPath("$.name").value(savedStudent.getName()))
                .andExpect(jsonPath("$.age").value(savedStudent.getAge()))
                .andDo(print());
    }

    @Test
    public void testGetStudent() throws Exception {
        long studentId = 1L;
        Student student = new Student("Гарри", 12);

        when(studentService.getStudent(studentId)).thenReturn(student);

        ResultActions perform = mockMvc.perform(get("/students/{id}",studentId));

        perform
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        long studentId = 1L;
        Student student = new Student("Гарри", 12);

        when(studentService.updateStudent(studentId, student)).thenReturn(student);

        ResultActions perform = mockMvc.perform(put("/students/{id}",studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        long studentId = 1L;
        Student student = new Student("Гарри", 12);

        when(studentService.deleteStudent(studentId)).thenReturn(student);

        ResultActions perform = mockMvc.perform(delete("/students/{id}",studentId));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
                .andDo(print());
    }

    @Test
    public void testFilterFaculties() throws Exception {
        int age = 12;
        List<Student> students = new ArrayList<>(List.of(new Student("Гарри", 12),
                new Student("Гермиона", 12)));
        when(studentService.filterByAge(age)).thenReturn(students);

        ResultActions perform = mockMvc.perform(get("/students").param("age",String.valueOf(age)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andDo(print());
    }

    @Test
    public void testFindByAgeAllBetween() throws Exception {
        List<Student> students = new ArrayList<>(List.of(new Student("Гарри", 12),
                new Student("Гермиона", 13)));

        when(studentService.findAllByAgeBetween(students.get(0).getAge(),
                students.get(1).getAge())).thenReturn(students);

        ResultActions perform = mockMvc.perform(get("/students/byAge?fromAge=12&toAge=13"));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andDo(print());
    }

    @Test
    public void testAllStudents() throws Exception {
        List<Student> students = new ArrayList<>(List.of(new Student("Гарри", 12),
                new Student("Гермиона", 13)));

        when(studentService.allStudent()).thenReturn(students);

        ResultActions perform = mockMvc.perform(get("/students/all"));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andDo(print());
    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");

        when(studentService.getFaculty(faculty.getId())).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/students/0/faculty"));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }
}

