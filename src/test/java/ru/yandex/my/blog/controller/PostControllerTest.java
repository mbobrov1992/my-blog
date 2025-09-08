package ru.yandex.my.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.my.blog.IntegrationTest;
import ru.yandex.my.blog.model.entity.PostEnt;
import ru.yandex.my.blog.repository.PostRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class PostControllerTest extends IntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void get_shouldRedirectToPosts() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        PostEnt postEnt = savePostEnt();

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts-feed"))
                .andExpect(model().attributeExists("posts", "search", "paging"))
                .andExpect(content().string(containsString(postEnt.getTitle())));
    }

    @Test
    void getPost_shouldReturnHtmlWithPost() throws Exception {
        PostEnt postEnt = savePostEnt();

        mockMvc.perform(get("/posts/" + postEnt.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(content().string(containsString(postEnt.getTitle())));
    }

    @Test
    void addPost_shouldCreatePostAndRedirectToPostId() throws Exception {
        MockMultipartFile mockImage = createMockImage("image-name.jpg");

        mockMvc.perform(multipart("/posts")
                        .file(mockImage)
                        .param("title", "Тема статьи")
                        .param("text", "Контент")
                        .param("tags", "тег1, тег2")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/posts/*"));

        List<PostEnt> postEnts = postRepository.findAll();

        assertEquals(1, postEnts.size());
        assertEquals("Тема статьи", postEnts.getFirst().getTitle());
        assertEquals("Контент", postEnts.getFirst().getText());
        assertEquals("тег1, тег2", postEnts.getFirst().getTags());
        assertEquals(mockImage.getOriginalFilename(), postEnts.getFirst().getImageName());
    }

    @Test
    void editPost_shouldUpdatePostAndRedirectToPostId() throws Exception {
        PostEnt originalPostEnt = savePostEnt();

        MockMultipartFile updatedMockImage = createMockImage("new-image-name.jpg");

        mockMvc.perform(multipart("/posts/" + originalPostEnt.getId())
                        .file(updatedMockImage)
                        .param("title", "Новая тема статьи")
                        .param("text", "Новый контент")
                        .param("tags", "тег3, тег4")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + originalPostEnt.getId()));

        List<PostEnt> postEnts = postRepository.findAll();
        assertEquals(1, postEnts.size());

        PostEnt updatedPostEnt = postEnts.getFirst();
        assertEquals(originalPostEnt.getId(), updatedPostEnt.getId());
        assertEquals("Новая тема статьи", updatedPostEnt.getTitle());
        assertEquals("Новый контент", updatedPostEnt.getText());
        assertEquals("тег3, тег4", updatedPostEnt.getTags());
        assertEquals(updatedMockImage.getOriginalFilename(), updatedPostEnt.getImageName());
    }

    @Test
    void deletePost_shouldRemovePostAndRedirectToPosts() throws Exception {
        PostEnt postEnt = savePostEnt();

        mockMvc.perform(post("/posts/" + postEnt.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        List<PostEnt> postEnts = postRepository.findAll();

        assertEquals(0, postEnts.size());
    }

    private PostEnt savePostEnt() {
        PostEnt postEnt = new PostEnt();
        postEnt.setTitle("Тема статьи");
        postEnt.setText("Контент");
        postEnt.setTags("тег1, тег2");
        postEnt.setImageName("image.jpg");

        return postRepository.save(postEnt);
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
