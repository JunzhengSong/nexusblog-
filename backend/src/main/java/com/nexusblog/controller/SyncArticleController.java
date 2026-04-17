package com.nexusblog.controller;

import com.nexusblog.entity.SyncArticleMapping;
import com.nexusblog.service.GithubSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/github/sync-articles")
public class SyncArticleController {

    private final GithubSyncService githubSyncService;

    public SyncArticleController(GithubSyncService githubSyncService) {
        this.githubSyncService = githubSyncService;
    }

    /**
     * 分页查询所有同步文章
     */
    @GetMapping
    public ResponseEntity<java.util.List<SyncArticleMapping>> getSyncArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        java.util.List<SyncArticleMapping> mappings = githubSyncService.getSyncArticleMappingPage(page, size);
        return ResponseEntity.ok(mappings);
    }

    /**
     * 查询指定仓库的同步文章
     */
    @GetMapping("/repo/{repoId}")
    public ResponseEntity<java.util.List<SyncArticleMapping>> getSyncArticlesByRepoId(@PathVariable Long repoId) {
        java.util.List<SyncArticleMapping> mappings = githubSyncService.getSyncArticleMappingByRepoConfigId(repoId);
        return ResponseEntity.ok(mappings);
    }

    /**
     * 删除同步文章（同时删除文章和映射关系）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSyncArticle(@PathVariable Long id) {
        SyncArticleMapping mapping = githubSyncService.getSyncArticleMappingByArticleId(id);
        if (mapping != null) {
            githubSyncService.deleteSyncArticle(id);
            return ResponseEntity.noContent().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 根据文章ID查询同步映射
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<SyncArticleMapping> getMappingByArticleId(@PathVariable Long articleId) {
        SyncArticleMapping mapping = githubSyncService.getSyncArticleMappingByArticleId(articleId);
        if (mapping != null) {
            return ResponseEntity.ok(mapping);
        }
        return ResponseEntity.notFound().build();
    }
}
