package ru.yandex.my.blog;

import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.yandex.my.blog.config.TestcontainersConfig;

import java.nio.file.Path;

@SpringBootTest
@Import(TestcontainersConfig.class)
public class IntegrationTest {

    @TempDir
    private static Path tempDir;

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("file.upload-dir", () -> tempDir.toString());
    }
}
