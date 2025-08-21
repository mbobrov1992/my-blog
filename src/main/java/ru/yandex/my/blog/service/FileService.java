package ru.yandex.my.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String fileUploadDir;

    public void save(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();

        if (multipartFile.isEmpty() || fileName == null) {
            throw new IllegalStateException("Unable to save file to disk: file is empty or name is missing");
        }

        log.info("Saving to disk file with name: {}", fileName);

        Path dirPath = Paths.get(fileUploadDir);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                log.info("File upload directory created: {}", dirPath.toAbsolutePath());
            }

            Path filePath = dirPath.resolve(fileName);
            multipartFile.transferTo(filePath);

            log.info("File saved successfully: {}", fileName);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save file to disk", e);
        }
    }
}
