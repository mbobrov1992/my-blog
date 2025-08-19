package ru.yandex.my.blog.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.my.blog.model.dto.CommentDto;
import ru.yandex.my.blog.model.entity.CommentEnt;

@Component
public class CommentMapper {

    public CommentDto toDto(CommentEnt entity) {
        return new CommentDto(
                entity.getId(),
                entity.getText(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
