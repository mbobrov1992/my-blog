package ru.yandex.my.blog.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileServiceTest {

    @TempDir
    private static Path tempDir;

    private static FileService fileService;

    @BeforeAll
    static void setUp() {
        fileService = new FileService(tempDir.toString());
    }

    @Test
    void save_shouldWriteFileToDisk() {
        String fileName = "file-save-test.jpg";
        fileService.save(createMockFile(fileName));

        Path path = Path.of(tempDir.toString(), fileName);
        assertTrue(Files.exists(path));
    }

    @Test
    void get_shouldReadFileFromDisk() {
        String fileName = "file-get-test.jpg";
        fileService.save(createMockFile(fileName));

        byte[] fileContent = fileService.get(fileName);

        assertNotNull(fileContent);
    }

    @Test
    void delete_shouldRemoveFileFromDisk() {
        String fileName = "file-delete-test.jpg";
        fileService.save(createMockFile(fileName));

        fileService.delete(fileName);

        Path path = Path.of(tempDir.toString(), fileName);
        assertFalse(Files.exists(path));
    }

    private MockMultipartFile createMockFile(String fileName) {
        return new MockMultipartFile(
                "file",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                "file content".getBytes()
        );
    }
}
