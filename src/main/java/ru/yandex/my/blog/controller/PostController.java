package ru.yandex.my.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.my.blog.model.dto.PostDto;
import ru.yandex.my.blog.service.PostService;

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

    @GetMapping("/posts/add")
    public String getAddPost() {
        return "add-post";
    }
}
