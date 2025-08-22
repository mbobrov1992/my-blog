package ru.yandex.my.blog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.my.blog.IntegrationTest;
import ru.yandex.my.blog.config.PostServiceConfiguration;
import ru.yandex.my.blog.model.dto.PostDto;
import ru.yandex.my.blog.model.dto.PostRequestDto;
import ru.yandex.my.blog.model.entity.PostEnt;
import ru.yandex.my.blog.repository.PostRepository;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(classes = PostServiceConfiguration.class)
public class PostServiceTest extends IntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @MockitoBean
    private FileService fileService;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void addPost_shouldAddPostToDatabaseAndSaveImageToDisk() {
        PostRequestDto requestDto = createPostRequestDto();

        postService.addPost(requestDto);

        List<PostEnt> postEnts = postRepository.findAll();

        assertEquals(1, postEnts.size());

        PostEnt postEnt = postEnts.getFirst();
        assertEquals(requestDto.title(), postEnt.getTitle());
        assertEquals(requestDto.text(), postEnt.getText());
        assertEquals(requestDto.tags(), postEnt.getTags());
        assertEquals(requestDto.image().getOriginalFilename(), postEnt.getImageName());

        verify(fileService, times(1)).save(requestDto.image());
    }

    @Test
    void editPost_shouldUpdatePostInDatabaseAndSaveImageToDiskAndDeleteOldImageFromDisk() {
        PostRequestDto createRequestDto = createPostRequestDto();
        PostDto postDto = postService.addPost(createRequestDto);

        PostRequestDto updateRequestDto = createPostRequestDto();
        postService.editPost(postDto.id(), updateRequestDto);

        List<PostEnt> postEnts = postRepository.findAll();

        assertEquals(1, postEnts.size());

        PostEnt postEnt = postEnts.getFirst();
        assertEquals(updateRequestDto.title(), postEnt.getTitle());
        assertEquals(updateRequestDto.text(), postEnt.getText());
        assertEquals(updateRequestDto.tags(), postEnt.getTags());
        assertEquals(updateRequestDto.image().getOriginalFilename(), postEnt.getImageName());

        verify(fileService, times(1)).save(createRequestDto.image());
        verify(fileService, times(1)).delete(createRequestDto.image().getOriginalFilename());
    }

    @Test
    void deletePost_shouldRemovePostFromDatabaseAndRemoveImageFromDisk() {
        PostDto postDto = postService.addPost(createPostRequestDto());

        postService.deletePost(postDto.id());

        List<PostEnt> postEnts = postRepository.findAll();
        assertEquals(0, postEnts.size());

        verify(fileService, times(1)).delete(postDto.imageName());
    }

    private PostRequestDto createPostRequestDto() {
        return new PostRequestDto(
                "Тема статьи",
                "Контент",
                createMockImage(new Random().nextInt() + "-image-name.jpg"),
                "тег1, тег2"
        );
    }

    private MockMultipartFile createMockImage(String fileName) {
        return new MockMultipartFile(
                "image",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                "image content".getBytes()
        );
    }
}
