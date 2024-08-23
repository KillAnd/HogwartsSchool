package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

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
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(long id, Faculty faculty) {
        return facultyRepository.findById(id).map(faculty1 -> {
            faculty1.setName(faculty.getName());
            faculty1.setColor(faculty.getColor());
            facultyRepository.save(faculty1);
            return faculty1;
        }).orElse(null);
    }

    public Faculty deleteFaculty(long id) {
        return facultyRepository.findById(id).map(faculty -> {
            facultyRepository.deleteById(id);
            return faculty;
        }).orElse(null);
    }

    public List<Faculty> allListFaculty() {
        return facultyRepository.findAll();
    }

    public List<Faculty> filterByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }
    public List<Faculty> findByColorOrName(String name, String color) {
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name ,color);
    }

    public List<Student> findStudentByFaculty(long id) {
        return facultyRepository.findById(id)
                .map(Faculty::getStudents)
                .orElse(null);
    }

}
