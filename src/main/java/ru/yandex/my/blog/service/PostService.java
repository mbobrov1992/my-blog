package ru.yandex.my.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.my.blog.mapper.PostMapper;
import ru.yandex.my.blog.model.dto.PostDto;
import ru.yandex.my.blog.repository.PostRepository;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public Page<PostDto> getPosts(String tag, Pageable pageable) {
        return postRepository.findAllByTagsContaining(tag, pageable)
                .map(postMapper::toDto);
    }

    public PostDto getPost(Long id) {
        return postRepository.findById(id)
                .map(postMapper::toDto)
                .orElseThrow();
    }
}
