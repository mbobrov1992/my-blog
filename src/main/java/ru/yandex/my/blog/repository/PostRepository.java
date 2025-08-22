package ru.yandex.my.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.my.blog.model.entity.PostEnt;

@Repository
public interface PostRepository extends JpaRepository<PostEnt, Long> {

    Page<PostEnt> findAllByTagsContaining(String tag, Pageable pageable);

    boolean existsByImageName(String imageName);
}
