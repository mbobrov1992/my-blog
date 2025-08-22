package ru.yandex.my.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.yandex.my.blog.mapper.PostMapper;
import ru.yandex.my.blog.repository.CommentRepository;
import ru.yandex.my.blog.repository.LikeRepository;
import ru.yandex.my.blog.repository.PostRepository;
import ru.yandex.my.blog.service.FileService;
import ru.yandex.my.blog.service.PostService;

@Import({
        DataSourceConfiguration.class,
        MapperConfiguration.class
})
@Configuration
public class PostServiceConfiguration {

    @Bean
    public PostService postService(
            PostRepository postRepository,
            LikeRepository likeRepository,
            CommentRepository commentRepository,
            PostMapper postMapper,
            FileService fileService
    ) {
        return new PostService(
                postRepository,
                likeRepository,
                commentRepository,
                postMapper,
                fileService
        );
    }
}
