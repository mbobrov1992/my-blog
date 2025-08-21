package ru.yandex.my.blog.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.my.blog.model.dto.PostDto;
import ru.yandex.my.blog.model.dto.PostRequestDto;
import ru.yandex.my.blog.model.entity.PostEnt;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class PostMapper {

    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;

    public PostEnt toEntity(PostRequestDto dto) {
        PostEnt entity = new PostEnt();

        entity.setTitle(dto.title());
        entity.setText(dto.text());
        entity.setTags(dto.tags());

        if (dto.image() != null) {
            entity.setImageName(dto.image().getOriginalFilename());
        }

        return entity;
    }

    public PostDto toDto(PostEnt entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getImageName(),
                entity.getText(),
                Arrays.stream(entity.getTags().split(","))
                        .map(String::trim)
                        .toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLikes().stream().map(likeMapper::toDto).toList(),
                entity.getComments().stream().map(commentMapper::toDto).toList()
        );
    }
}
