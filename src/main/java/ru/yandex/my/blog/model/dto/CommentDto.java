package ru.yandex.my.blog.model.dto;

import java.time.LocalDateTime;

public record CommentDto(
        long id,
        String text,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
