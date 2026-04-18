package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import com.nexusblog.dto.SyncHistoryDTO;
import com.nexusblog.service.GithubSyncService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/github/sync")
public class GithubSyncController {

    private final GithubSyncService githubSyncService;

    public GithubSyncController(GithubSyncService githubSyncService) {
        this.githubSyncService = githubSyncService;
    }

    @PostMapping("/{repoId}")
    public ApiResult<Map<String, Object>> triggerSync(@PathVariable Long repoId) {
        Long syncId = githubSyncService.startSync(repoId);

        Map<String, Object> result = new HashMap<>();
        result.put("syncId", syncId);
        result.put("status", "PENDING");
        result.put("message", "同步任务已提交，后台执行中");

        return ApiResult.ok(result);
    }

    @GetMapping("/status/{syncId}")
    public ApiResult<SyncHistoryDTO> getSyncStatus(@PathVariable Long syncId) {
        com.nexusblog.entity.SyncHistory syncHistory = githubSyncService.getSyncHistoryById(syncId);
        if (syncHistory == null) {
            return ApiResult.error(404, "同步记录不存在");
        }

        SyncHistoryDTO dto = SyncHistoryDTO.builder()
                .id(syncHistory.getId())
                .repoConfigId(syncHistory.getRepoConfigId())
                .status(syncHistory.getStatus())
                .totalFiles(syncHistory.getTotalFiles())
                .successCount(syncHistory.getSuccessCount())
                .failCount(syncHistory.getFailCount())
                .errorMessage(syncHistory.getErrorMessage())
                .startTime(syncHistory.getStartTime())
                .endTime(syncHistory.getEndTime())
                .createTime(syncHistory.getCreateTime())
                .build();

        return ApiResult.ok(dto);
    }

    @GetMapping("/history")
    public ApiResult<List<SyncHistoryDTO>> getSyncHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<SyncHistoryDTO> history = githubSyncService.getSyncHistoryPage(page, size);
        return ApiResult.ok(history);
    }

    @GetMapping("/history/repo/{repoId}")
    public ApiResult<List<com.nexusblog.entity.SyncHistory>> getSyncHistoryByRepoConfigId(@PathVariable Long repoId) {
        List<com.nexusblog.entity.SyncHistory> history = githubSyncService.getSyncHistoryByRepoConfigId(repoId);
        return ApiResult.ok(history);
    }
}
