package ru.yandex.my.blog.model.dto;

import java.time.LocalDateTime;

public record LikeDto(
        long id,
        LocalDateTime createdAt
) {
}
