package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final int BUFFER_SIZE = 1024;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Был запущен метод по загрузке аватара студента " + studentId);
        Student student = studentRepository.findById(studentId).orElseThrow(() ->
                new IllegalArgumentException("Студент с id " + studentId + " отсутствует в базе данных"));

        Path avatarPath = saveToLocalDirectory(student, avatarFile);
        Avatar avatar = saveToDataBased(student, avatarPath, avatarFile);

    }

    public Path saveToLocalDirectory(Student student, MultipartFile avatarFile) throws IOException {
        logger.info("Аватар студента добавлен в локальную директорию");
        Path avatarPath = Path
                .of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(avatarPath.getParent());
        Files.deleteIfExists(avatarPath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(avatarPath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        ) {
            bis.transferTo(bos);
        }

        return avatarPath;
    }

    public Avatar saveToDataBased(Student student, Path avatarPath, MultipartFile avatarFile) throws IOException{
        logger.info("Аватар добавлен в базу данных");
        Avatar avatar = getAvatarStudent(student);
        avatar.setStudent(student);
        avatar.setFilePath(avatarPath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        return avatarRepository.save(avatar);
    }

    public Avatar getAvatarStudent(Student student) {
        logger.info("Был запущен метод по получению аватара из базы данных");
        return avatarRepository.findByStudent(student).orElseGet(() -> {
            Avatar avatar = new Avatar();
            avatar.setStudent(student);
            return avatar;
        });
    }

    public Avatar findAvatar(long id) {
        logger.info("Был запущен метод по получению аватара из базы данных по идентификатору id" + id);
        return avatarRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Студент с id " + id + " отсутствует в базе данных"));
    }
    private String getExtensions(String fileName) {
        logger.info("Извлечение расширения файла");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<Avatar> findAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Вызван метод получения всех аватарок с использованием пагинации");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }


    
}

