package ru.yandex.my.blog.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
        long id,
        String title,
        String imageName,
        String text,
        List<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<LikeDto> likes,
        List<CommentDto> comments
) {
    public String getTextPreview() {
        final int maxLength = 60;

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength + 1) + "...";
    }

    public String[] getTextParts() {
        return text.split("\\r?\\n\\r?\\n");
    }

    public String getTagsAsText() {
        return String.join(", ", tags);
    }
}
