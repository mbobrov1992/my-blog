package ru.yandex.my.blog.model.dto;

import org.springframework.web.multipart.MultipartFile;

public record PostRequestDto(
        String title,
        String text,
        MultipartFile image,
        String tags
) {
}
