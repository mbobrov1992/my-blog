package ru.yandex.my.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.my.blog.mapper.PostMapper;
import ru.yandex.my.blog.model.dto.PostDto;
import ru.yandex.my.blog.model.dto.PostRequestDto;
import ru.yandex.my.blog.model.entity.LikeEnt;
import ru.yandex.my.blog.model.entity.PostEnt;
import ru.yandex.my.blog.repository.LikeRepository;
import ru.yandex.my.blog.repository.PostRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper;
    private final FileService fileService;

    public Page<PostDto> getPosts(String tag, Pageable pageable) {
        return postRepository.findAllByTagsContaining(tag, pageable)
                .map(postMapper::toDto);
    }

    public PostDto getPost(Long id) {
        return postRepository.findById(id)
                .map(postMapper::toDto)
                .orElseThrow();
    }

    public byte[] getImage(Long id) {
        String fileName = postRepository.findById(id)
                .orElseThrow()
                .getImageName();

        return fileName == null ? null : fileService.get(fileName);
    }

    public PostDto addPost(PostRequestDto request) {
        if (request.image() != null && !request.image().isEmpty()) {
            fileService.save(request.image());
        }

        PostEnt postEnt = postMapper.toEntity(request);

        postEnt = postRepository.save(postEnt);

        return postMapper.toDto(postEnt);
    }

    public PostDto editPost(Long id, PostRequestDto request) {
        PostEnt postEnt = postRepository.findById(id)
                .orElseThrow();

        if (request.image() != null && !request.image().isEmpty()) {
            fileService.save(request.image());
            postEnt.setImageName(request.image().getOriginalFilename());
        }

        postEnt.setTitle(request.title());
        postEnt.setText(request.text());
        postEnt.setTags(request.tags());

        postEnt = postRepository.save(postEnt);

        return postMapper.toDto(postEnt);
    }

    public void like(Long id, boolean isLike) {
        PostEnt postEnt = postRepository.findById(id)
                .orElseThrow();

        if (isLike) {
            LikeEnt like = new LikeEnt();
            like.setPost(postEnt);
            likeRepository.save(like);
        } else {
            List<LikeEnt> likes = postEnt.getLikes();

            if (!likes.isEmpty()) {
                LikeEnt like = likes.getFirst();
                likeRepository.delete(like);
            }
        }
    }
}
