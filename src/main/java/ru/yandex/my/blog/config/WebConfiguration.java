package ru.yandex.my.blog.config;

import org.springframework.context.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "ru.yandex.my.blog")
@Configuration
public class WebConfiguration {

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
