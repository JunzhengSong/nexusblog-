package com.nexusblog.service;

import com.nexusblog.dto.ArticleDTO;
import com.nexusblog.dto.ArticleRequestDTO;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.Article;
import com.nexusblog.entity.Category;
import com.nexusblog.entity.Tag;
import com.nexusblog.exception.ResourceNotFoundException;
import com.nexusblog.repository.ArticleRepository;
import com.nexusblog.repository.CategoryRepository;
import com.nexusblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final DtoMapper dtoMapper;

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc();
        return dtoMapper.toArticleDTOList(articles);
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id));
        return dtoMapper.toArticleDTO(article);
    }

    @Transactional
    public ArticleDTO createArticle(ArticleRequestDTO request) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());

        // Set category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            article.setCategory(category);
        }

        // Set tags if provided
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            Set<Tag> tags = tagRepository.findAllById(request.getTagIds())
                    .stream()
                    .collect(java.util.stream.Collectors.toSet());
            article.setTags(new java.util.ArrayList<>(tags));
        }

        Article savedArticle = articleRepository.save(article);
        return dtoMapper.toArticleDTO(savedArticle);
    }

    @Transactional
    public ArticleDTO updateArticle(Long id, ArticleRequestDTO request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id));

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }

        // Update tags if provided
        if (request.getTagIds() != null) {
            Set<Tag> tags = tagRepository.findAllById(request.getTagIds())
                    .stream()
                    .collect(java.util.stream.Collectors.toSet());
            article.setTags(new java.util.ArrayList<>(tags));
        }

        Article savedArticle = articleRepository.save(article);
        return dtoMapper.toArticleDTO(savedArticle);
    }

    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article", id);
        }
        articleRepository.deleteById(id);
    }
}
