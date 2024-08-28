package ru.hogwarts.school.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @MockBean
    private FacultyService facultyService;
    @MockBean
    private AvatarService avatarService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateStudent() throws Exception {
        long facultyId = 1L;
        Faculty faculty = new Faculty("Гриффиндор","Оранжевый");
        Faculty savedFaculty = new Faculty("Гриффиндор","Оранжевый");
        when(facultyService.createFaculty(faculty)).thenReturn(savedFaculty);
        savedFaculty.setId(facultyId);

        ResultActions perform = mockMvc.perform(post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedFaculty.getId()))
                .andExpect(jsonPath("$.name").value(savedFaculty.getName()))
                .andExpect(jsonPath("$.color").value(savedFaculty.getColor()))
                .andDo(print());
    }

    @Test
    public void testGetFaculty() throws Exception {
        long facultyId = 1L;
        Faculty faculty = new Faculty("Гриффиндор","Оранжевый");

        when(facultyService.readFaculty(facultyId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(get("/faculty/{id}",facultyId));

        perform
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        long facultyId = 1L;
        Faculty faculty = new Faculty("Гриффиндор","Оранжевый");

        when(facultyService.updateFaculty(facultyId, faculty)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(put("/faculty/{id}",facultyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        long facultyId = 1L;
        Faculty faculty = new Faculty("Гриффиндор","Оранжевый");

        when(facultyService.deleteFaculty(facultyId)).thenReturn(faculty);

        ResultActions perform = mockMvc.perform(delete("/faculty/{id}",facultyId));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
                .andDo(print());
    }

    @Test
    public void testFilterFaculties() throws Exception {
        String color = "Оранжевый";
        List<Faculty> faculties = new ArrayList<>(List.of(new Faculty("Гриффиндор", "Оранжевый"),
                new Faculty("Слизерин", "Зеленый")));
        when(facultyService.filterByColor(color)).thenReturn(faculties);

        ResultActions perform = mockMvc.perform(get("/faculty").param("color",color));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(faculties.get(0).getName()))
                .andDo(print());
    }

    @Test
    public void testFindFacultyByNameOrColor() throws Exception {
        String name = "Гриффиндор";
        String color = "Зеленый";
        List<Faculty> faculties = new ArrayList<>(List.of(new Faculty("Гриффиндор", "Оранжевый"),
                new Faculty("Слизерин", "Зеленый")));

        when(facultyService.findByColorOrName(faculties.get(0).getName(),
                faculties.get(1).getColor())).thenReturn(faculties);

        ResultActions perform = mockMvc.perform(get("/faculty/nameOrColor")
                .param("name",name)
                .param("color",color));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(faculties.get(0).getName()))
                .andExpect(jsonPath("$[1].color").value(faculties.get(1).getColor()))
                .andDo(print());
    }

    @Test
    public void testAllFaculties() throws Exception {
        List<Faculty> faculties = new ArrayList<>(List.of(new Faculty("Гриффиндор", "Оранжевый"),
                new Faculty("Слизерин", "Зеленый")));

        when(facultyService.allListFaculty()).thenReturn(faculties);

        ResultActions perform = mockMvc.perform(get("/faculty/all"));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(faculties.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(faculties.get(1).getName()))
                .andDo(print());
    }

    @Test
    public void testGetStudentsByFaculties() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Оранжевый");

        List<Student> students = new ArrayList<>(List.of(new Student("Гарри", 12,faculty),
                new Student("Гермиона", 13,faculty)));


        when(facultyService.findStudentByFaculty(students.get(0).getId())).thenReturn(students);

        ResultActions perform = mockMvc.perform(get("/faculty/0/studentsByFaculty"));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(students.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(students.get(1).getName()))
                .andDo(print());
    }

}
