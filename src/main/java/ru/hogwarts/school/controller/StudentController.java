package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student update = studentService.updateStudent(id, student);
        if (update == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        Student deleted = studentService.deleteStudent(id);
        if (deleted == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }

    @GetMapping
    public List<Student> filterFaculties(@RequestParam int age) {
        return studentService.filterByAge(age);
    }

    @GetMapping("/byAge")
    public List<Student> findByAgeAllBetween(int fromAge, int toAge) {
        return studentService.findAllByAgeBetween(fromAge, toAge);
    }

    @GetMapping("/all")
    public List<Student> allStudents() {
        return studentService.allStudent();
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFaculty(@PathVariable long id) {
        return studentService.getFaculty(id);
    }

}
