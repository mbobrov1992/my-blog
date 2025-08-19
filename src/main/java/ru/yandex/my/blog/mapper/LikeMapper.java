package ru.yandex.my.blog.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.my.blog.model.dto.LikeDto;
import ru.yandex.my.blog.model.entity.LikeEnt;

@Component
public class LikeMapper {

    public LikeDto toDto(LikeEnt entity) {
        return new LikeDto(
                entity.getId(),
                entity.getCreatedAt()
        );
    }
}
