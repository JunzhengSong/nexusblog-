package com.nexusblog.controller;

import com.nexusblog.dto.SyncHistoryDTO;
import com.nexusblog.service.GithubSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/github/sync")
public class GithubSyncController {

    private final GithubSyncService githubSyncService;

    public GithubSyncController(GithubSyncService githubSyncService) {
        this.githubSyncService = githubSyncService;
    }

    /**
     * 触发同步
     */
    @PostMapping("/{repoId}")
    public ResponseEntity<Map<String, Object>> triggerSync(@PathVariable Long repoId) {
        Long syncId = githubSyncService.startSync(repoId);

        Map<String, Object> result = new HashMap<>();
        result.put("syncId", syncId);
        result.put("status", "PENDING");
        result.put("message", "同步任务已提交，后台执行中");

        return ResponseEntity.ok(result);
    }

    /**
     * 查询同步状态
     */
    @GetMapping("/status/{syncId}")
    public ResponseEntity<SyncHistoryDTO> getSyncStatus(@PathVariable Long syncId) {
        // TODO: 实现从Service获取同步状态的逻辑
        return ResponseEntity.notFound().build();
    }

    /**
     * 分页查询所有同步历史
     */
    @GetMapping("/history")
    public ResponseEntity<java.util.List<SyncHistoryDTO>> getSyncHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        java.util.List<SyncHistoryDTO> history = githubSyncService.getSyncHistoryPage(page, size);
        return ResponseEntity.ok(history);
    }

    /**
     * 查询指定仓库的同步历史
     */
    @GetMapping("/history/repo/{repoId}")
    public ResponseEntity<java.util.List<com.nexusblog.entity.SyncHistory>> getSyncHistoryByRepoRepoConfigId(@PathVariable Long repoId) {
        java.util.List<com.nexusblog.entity.SyncHistory> history = githubSyncService.getSyncHistoryByRepoConfigId(repoId);
        return ResponseEntity.ok(history);
    }
}
