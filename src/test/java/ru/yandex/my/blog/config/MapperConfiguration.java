package ru.yandex.my.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.my.blog.mapper.CommentMapper;
import ru.yandex.my.blog.mapper.LikeMapper;
import ru.yandex.my.blog.mapper.PostMapper;

@Configuration
public class MapperConfiguration {

    @Bean
    public LikeMapper likeMapper() {
        return new LikeMapper();
    }

    @Bean
    public CommentMapper commentMapper() {
        return new CommentMapper();
    }

    @Bean
    public PostMapper postMapper(LikeMapper likeMapper, CommentMapper commentMapper) {
        return new PostMapper(likeMapper, commentMapper);
    }
}
