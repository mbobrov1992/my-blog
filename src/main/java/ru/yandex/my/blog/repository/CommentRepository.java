package ru.yandex.my.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.my.blog.model.entity.CommentEnt;

@Repository
public interface CommentRepository extends JpaRepository<CommentEnt, Long> {
}
