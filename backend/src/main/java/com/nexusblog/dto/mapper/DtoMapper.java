package com.nexusblog.dto.mapper;

import com.nexusblog.dto.*;
import com.nexusblog.entity.Article;
import com.nexusblog.entity.Category;
import com.nexusblog.entity.Tag;
import com.nexusblog.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .createdAt(user.getCreateTime())
                .build();
    }

    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .articleCount(category.getArticles() != null ? (long) category.getArticles().size() : 0L)
                .build();
    }

    public List<CategoryDTO> toCategoryDTOList(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public TagDTO toTagDTO(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagDTO.builder()
                .id(tag.getId())
                .name(tag.getName())
                .articleCount(tag.getArticles() != null ? (long) tag.getArticles().size() : 0L)
                .build();
    }

    public List<TagDTO> toTagDTOList(List<Tag> tags) {
        return tags.stream()
                .map(this::toTagDTO)
                .collect(Collectors.toList());
    }

    public ArticleDTO toArticleDTO(Article article) {
        if (article == null) {
            return null;
        }
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .category(toCategoryDTO(article.getCategory()))
                .tags(toTagDTOList(article.getTags()))
                .createdAt(article.getCreateTime())
                .updatedAt(article.getUpdateTime())
                .build();
    }

    public List<ArticleDTO> toArticleDTOList(List<Article> articles) {
        return articles.stream()
                .map(this::toArticleDTO)
                .collect(Collectors.toList());
    }
}
