package ru.yandex.my.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.my.blog.model.dto.PostDto;
import ru.yandex.my.blog.model.dto.PostRequestDto;
import ru.yandex.my.blog.service.PostService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String get() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String getPosts(
            Model model,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostDto> posts = postService.getPosts(search, pageable);

        model.addAttribute("posts", posts.get().toList());
        model.addAttribute("search", search);
        model.addAttribute("paging", posts);

        return "posts";
    }

    @GetMapping("/posts/{id}")
    public String getPost(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        PostDto post = postService.getPost(id);

        model.addAttribute("post", post);

        return "post";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable(name = "id") Long id) {
        byte[] fileContent = postService.getImage(id);

        return new ResponseEntity<>(fileContent, HttpStatus.OK);
    }

    @GetMapping("/posts/add")
    public String getAddPost() {
        return "add-post";
    }

    @GetMapping(path = "/posts/{id}/edit")
    public String getEditPost(
            Model model,
            @PathVariable(name = "id") Long id
    ) {
        PostDto post = postService.getPost(id);

        model.addAttribute("post", post);

        return "add-post";
    }

    @PostMapping(path = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addPost(
            @ModelAttribute PostRequestDto request,
            Model model
    ) {
        log.info("Post creation request received: {}", request);

        PostDto post = postService.addPost(request);

        model.addAttribute("post", post);

        return "redirect:/posts/" + post.id();
    }

    @PostMapping(path = "/posts/{id}/like")
    public String like(
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "like") boolean isLike
    ) {
        log.info("{} post with id: {}", isLike ? "Liked" : "Disliked", id);

        postService.like(id, isLike);

        return "redirect:/posts/" + id;
    }
}
