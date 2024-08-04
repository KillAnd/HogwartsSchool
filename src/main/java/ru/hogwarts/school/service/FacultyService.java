package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyList = new HashMap<>();
    private long idCounter = 1L;

    public Faculty createFaculty(Faculty faculty) {
        facultyList.put(faculty.getId(),faculty);
        faculty.setId(idCounter++);
        return faculty;
    }

    public Faculty readFaculty(long id) {
        return facultyList.get(id);
    }

    public Faculty updateFaculty(long id, Faculty faculty) {
        facultyList.put(id, faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        return facultyList.remove(id);
    }

    public Map<Long, Faculty> allListFaculty() {
        return facultyList;
    }

    public List<Faculty> filterByColor(String color) {
        return facultyList.values()
                .stream().
                filter(it -> it.getColor().equals(color)).
                collect(Collectors.toList());
    }

}
