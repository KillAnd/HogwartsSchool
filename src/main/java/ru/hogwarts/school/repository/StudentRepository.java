package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int fromAge, int toAge);
    @Query(value = "SELECT COUNT(*) FROM students", nativeQuery = true)
    Long findAllStudent();

    @Query(value = "SELECT AVG(age) FROM students", nativeQuery = true)
    Long findAVGAgeStudents();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getFiveLastStudents();




}
