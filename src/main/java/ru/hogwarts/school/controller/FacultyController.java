package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        Faculty faculty = facultyService.readFaculty(id);
        if (faculty == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        Faculty update = facultyService.updateFaculty(id, faculty);
        if (update == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable long id) {
        Faculty deleted = facultyService.deleteFaculty(id);
        if (deleted == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }

    @GetMapping
    public List<Faculty> filterFaculties(@RequestParam String color) {
        return facultyService.filterByColor(color);
    }
    @GetMapping("/nameOrColor")
    public List<Faculty> findByNameOrColor(String name, String color) {
        return facultyService.findByColorOrName(name ,color);
    }

    @GetMapping("/all")
    public List<Faculty> allFaculty() {
        return facultyService.allListFaculty();
    }

    @GetMapping("/{id}/studentsByFaculty")
    public List<Student> getStudentsByFaculty(@PathVariable long id) {
        return facultyService.findStudentByFaculty(id);
    }

    @GetMapping("/LongLongNameFaculty")
    public String longLongName() {
        return facultyService.longLongNameFaculty();
    }

    @GetMapping("/intSum")
    public String intSum() {
        return facultyService.sumInt();
    }
}
