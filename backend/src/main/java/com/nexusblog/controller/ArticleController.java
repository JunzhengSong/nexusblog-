package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import com.nexusblog.dto.ArticleDTO;
import com.nexusblog.dto.ArticleRequestDTO;
import com.nexusblog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ApiResult<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ApiResult.ok(articles);
    }

    @GetMapping("/{id}")
    public ApiResult<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        return ApiResult.ok(article);
    }

    @PostMapping
    public ApiResult<ArticleDTO> createArticle(@Valid @RequestBody ArticleRequestDTO request) {
        ArticleDTO article = articleService.createArticle(request);
        return ApiResult.ok(article, "创建成功");
    }

    @PutMapping("/{id}")
    public ApiResult<ArticleDTO> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequestDTO request) {
        ArticleDTO article = articleService.updateArticle(id, request);
        return ApiResult.ok(article, "更新成功");
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResult.noContent("删除成功");
    }
}
