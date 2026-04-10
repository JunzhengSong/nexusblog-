package com.nexusblog.repository;

import com.nexusblog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByOrderByCreatedAtDesc();

    List<Article> findByCategoryIdOrderByCreatedAtDesc(Long categoryId);

    List<Article> findByTagsIdOrderByCreatedAtDesc(Long tagId);
}
