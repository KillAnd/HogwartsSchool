package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty(long id) {
        return facultyRepository.getReferenceById(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Long deleteFaculty(long id) {
        facultyRepository.deleteById(id);
        return id;
    }

    public List<Faculty> allListFaculty() {
        return facultyRepository.findAll();
    }

    public List<Faculty> filterByColor(String color) {
        return facultyRepository.findAll().
                stream().
                filter(fil -> fil.getColor().equals(color)).
                collect(Collectors.toList());
    }

}
