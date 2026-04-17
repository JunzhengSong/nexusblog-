package com.nexusblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexusblog.dto.ArticleDTO;
import com.nexusblog.dto.ArticleRequestDTO;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.Article;
import com.nexusblog.entity.Category;
import com.nexusblog.entity.Tag;
import com.nexusblog.exception.ResourceNotFoundException;
import com.nexusblog.mapper.ArticleMapper;
import com.nexusblog.mapper.CategoryMapper;
import com.nexusblog.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final DtoMapper dtoMapper;

    public List<ArticleDTO> getAllArticles() {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Article> articles = articleMapper.selectList(wrapper);
        return dtoMapper.toArticleDTOList(articles);
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article", id);
        }
        return dtoMapper.toArticleDTO(article);
    }

    @Transactional
    public ArticleDTO createArticle(ArticleRequestDTO request) {
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categoryId(request.getCategoryId())
                .build();

        // 设置分类（通过categoryId直接设置）
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryMapper.selectById(request.getCategoryId());
            if (category == null) {
                throw new ResourceNotFoundException("Category", request.getCategoryId());
            }
            article.setCategory(category);
        }

        // 设置标签
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<Tag> tags = tagMapper.selectBatchIds(request.getTagIds());
            article.setTags(new ArrayList<>(tags));
        }

        articleMapper.insert(article);
        return dtoMapper.toArticleDTO(article);
    }

    @Transactional
    public ArticleDTO updateArticle(Long id, ArticleRequestDTO request) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article", id);
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCategoryId(request.getCategoryId());

        // 更新分类
        if (request.getCategoryId() != null) {
            Category category = categoryMapper.selectById(request.getCategoryId());
            if (category == null) {
                throw new ResourceNotFoundException("Category", request.getCategoryId());
            }
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }

        // 更新标签
        if (request.getTagIds() != null) {
            List<Tag> tags = tagMapper.selectBatchIds(request.getTagIds());
            article.setTags(new ArrayList<>(tags));
        }

        articleMapper.updateById(article);
        return dtoMapper.toArticleDTO(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article", id);
        }
        articleMapper.deleteById(id);
    }
}
