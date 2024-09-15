package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Был создан новый факультет");
        return facultyRepository.save(faculty);
    }

    public Faculty readFaculty(long id) {
        logger.info("Был запущен метод по получению факультета");
        logger.error("Не правильно указан id факультета");
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty updateFaculty(long id, Faculty faculty) {
        logger.info("Был изменен факультет");
        logger.error("Не был указан id факультета");
        return facultyRepository.findById(id).map(faculty1 -> {
            faculty1.setName(faculty.getName());
            faculty1.setColor(faculty.getColor());
            facultyRepository.save(faculty1);
            return faculty1;
        }).orElse(null);
    }

    public Faculty deleteFaculty(long id) {
        logger.info("Был удален факультет");
        logger.error("Не был указан id факультета");
        return facultyRepository.findById(id).map(faculty -> {
            facultyRepository.deleteById(id);
            return faculty;
        }).orElse(null);
    }

    public List<Faculty> allListFaculty() {
        logger.info("Был запущен метод вызова всех факультетов");
        return facultyRepository.findAll();
    }

    public List<Faculty> filterByColor(String color) {
        logger.info("Запущен метод фильтрации факультетов по цвету");
        logger.error("Не был указан цвет факультета");
        return facultyRepository.findAllByColor(color);
    }
    public List<Faculty> findByColorOrName(String name, String color) {
        logger.info("Запущен метод по поиску факультетов по цвету или имени");
        logger.error("Неверно указан один из параметров");
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name ,color);
    }

    public List<Student> findStudentByFaculty(long id) {
        logger.info("Запущен метод по поиску студентов из факультета " + id);
        logger.error("Неверно указан id");
        return facultyRepository.findById(id)
                .map(Faculty::getStudents)
                .orElse(null);
    }

    public String longLongNameFaculty() {
        logger.info("Запущен метод по поиску самого длинного имени в списке факультетов");
        return facultyRepository.findAll()
                .stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse(null);
    }

    public String sumInt() {
        Instant startTime1 = Instant.now();
        Long sum1 = Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000L)
                .reduce(0L, (a, b) -> a + b);
        Instant endTime1 = Instant.now();

        Instant startTime2 = Instant.now();
        Long sum2 = Stream.iterate(1L, a -> a + 1)
                .parallel()
                .limit(1_000_000L)
                .reduce(0L, (a, b) -> a + b);
        Instant endTime2 = Instant.now();

        Duration duration1 = Duration.between(startTime1, endTime1);
        Duration duration2 = Duration.between(startTime2, endTime2);
        
        String answer;

        if (duration1.toMillis() > duration2.toMillis()) {
            answer = "второй метод быстрее";
        } else {
            answer = "первый метод быстрее";
        }
        System.out.println(duration1);
        System.out.println(duration2);

        return answer;
    }

}
