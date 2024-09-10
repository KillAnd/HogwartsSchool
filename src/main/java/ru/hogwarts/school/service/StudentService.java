package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        logger.info("Запущен метод создания студента");
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        logger.info("Запущен метод получения студента" + id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Long id, Student student) {
        logger.info("Запущен метод изменения студента");
        return studentRepository.findById(id).map(student1 -> {
            student1.setName(student.getName());
            student1.setAge(student.getAge());
            studentRepository.save(student1);
            return student1;
        }).orElse(null);
    }
    public Student deleteStudent(long id) {
        logger.info("Запущен метод удаления студента");
        return studentRepository.findById(id).map(student -> {
            studentRepository.deleteById(id);
            return student;
        }).orElse(null);
    }

    public List<Student> filterByAge(int age) {
        logger.info("Запущен метод фильтрации студентов по возрасту");
        return studentRepository.findAllByAge(age);
    }
    public List<Student> findAllByAgeBetween(int fromAge, int toAge) {
        logger.info("Запущен метод фильтрации студентов по возрасту от меньшего к большему");
        return studentRepository.findAllByAgeBetween(fromAge,toAge);
    }

    public List<Student> allStudent() {
        logger.info("Запущен метод вызова всех студентов");
        return studentRepository.findAll();
    }

    public Faculty getFaculty(Long studentId) {
        logger.error("Ошибка, не правильно указан id студента");
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public Long findAllStudent() {
        logger.info("Был указан размер списка студентов");
        return studentRepository.findAllStudent();
    }

    public Long averageAgeStudents() {
        logger.info("Был запущен метод получения среднего возраста у студентов");
        return studentRepository.findAVGAgeStudents();
    }

    public List<Student> getLastStudents() {
        logger.info("Был вызван метод с указанием последних 5-ти студентов");
        return studentRepository.getFiveLastStudents();
    }

    public List<String> sortedToNameStudent() {
        logger.info("Запущен метод по сортировке студентов по имени(буква А)");
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public Double sortedToAgeStudent() {
        logger.info("Запущен метод по сортировке студентов по возрасту(среднее значение)");
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
}
