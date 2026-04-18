package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import com.nexusblog.entity.SyncArticleMapping;
import com.nexusblog.service.GithubSyncService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/github/sync-articles")
public class SyncArticleController {

    private final GithubSyncService githubSyncService;

    public SyncArticleController(GithubSyncService githubSyncService) {
        this.githubSyncService = githubSyncService;
    }

    @GetMapping
    public ApiResult<List<SyncArticleMapping>> getSyncArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<SyncArticleMapping> mappings = githubSyncService.getSyncArticleMappingPage(page, size);
        return ApiResult.ok(mappings);
    }

    @GetMapping("/repo/{repoId}")
    public ApiResult<List<SyncArticleMapping>> getSyncArticlesByRepoId(@PathVariable Long repoId) {
        List<SyncArticleMapping> mappings = githubSyncService.getSyncArticleMappingByRepoConfigId(repoId);
        return ApiResult.ok(mappings);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> deleteSyncArticle(@PathVariable Long id) {
        SyncArticleMapping mapping = githubSyncService.getSyncArticleMappingByArticleId(id);
        if (mapping != null) {
            githubSyncService.deleteSyncArticle(id);
            return ApiResult.noContent("删除成功");
        }
        return ApiResult.error(404, "同步文章不存在");
    }

    @GetMapping("/article/{articleId}")
    public ApiResult<SyncArticleMapping> getMappingByArticleId(@PathVariable Long articleId) {
        SyncArticleMapping mapping = githubSyncService.getSyncArticleMappingByArticleId(articleId);
        if (mapping != null) {
            return ApiResult.ok(mapping);
        }
        return ApiResult.error(404, "同步文章不存在");
    }
}
