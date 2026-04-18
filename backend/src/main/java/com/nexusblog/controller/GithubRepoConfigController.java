package com.nexusblog.controller;

import com.nexusblog.client.GithubClient;
import com.nexusblog.common.ApiResult;
import com.nexusblog.dto.GithubRepoConfigDTO;
import com.nexusblog.entity.GithubRepoConfig;
import com.nexusblog.service.GithubSyncService;
import com.nexusblog.util.AesEncryptUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/github/repos")
public class GithubRepoConfigController {

    private final GithubSyncService githubSyncService;
    private final GithubClient githubClient;

    public GithubRepoConfigController(GithubSyncService githubSyncService, GithubClient githubClient) {
        this.githubSyncService = githubSyncService;
        this.githubClient = githubClient;
    }

    @GetMapping
    public ApiResult<List<GithubRepoConfigDTO>> getAll() {
        List<GithubRepoConfig> configs = githubSyncService.getAllRepoConfigs();
        List<GithubRepoConfigDTO> dtos = configs.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ApiResult.ok(dtos);
    }

    @GetMapping("/{id}")
    public ApiResult<GithubRepoConfigDTO> getById(@PathVariable Long id) {
        GithubRepoConfig config = githubSyncService.getRepoConfigById(id);
        if (config == null) {
            return ApiResult.error(404, "仓库配置不存在");
        }
        return ApiResult.ok(convertToDTO(config));
    }

    @PostMapping
    public ApiResult<GithubRepoConfigDTO> create(@Valid @RequestBody GithubRepoConfigDTO dto) {
        GithubRepoConfig config = GithubRepoConfig.builder()
                .name(dto.getName())
                .repoUrl(dto.getRepoUrl())
                .branch(dto.getBranch())
                .docRootPath(dto.getDocRootPath())
                .defaultCategory(dto.getDefaultCategory())
                .defaultTags(dto.getDefaultTags())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .description(dto.getDescription())
                .build();

        if (dto.getAccessToken() != null && !dto.getAccessToken().isEmpty()) {
            config.setAccessToken(AesEncryptUtil.encrypt(dto.getAccessToken()));
        }

        githubSyncService.createRepoConfig(config);
        return ApiResult.ok(convertToDTO(config), "创建成功");
    }

    @PutMapping("/{id}")
    public ApiResult<GithubRepoConfigDTO> update(@PathVariable Long id, @Valid @RequestBody GithubRepoConfigDTO dto) {
        GithubRepoConfig config = githubSyncService.getRepoConfigById(id);
        if (config == null) {
            return ApiResult.error(404, "仓库配置不存在");
        }

        config.setName(dto.getName());
        config.setRepoUrl(dto.getRepoUrl());
        config.setBranch(dto.getBranch());
        config.setDocRootPath(dto.getDocRootPath());
        config.setDefaultCategory(dto.getDefaultCategory());
        config.setDefaultTags(dto.getDefaultTags());
        config.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
        config.setDescription(dto.getDescription());

        if (dto.getAccessToken() != null && !dto.getAccessToken().isEmpty()) {
            config.setAccessToken(AesEncryptUtil.encrypt(dto.getAccessToken()));
        }

        githubSyncService.updateRepoConfig(config);
        return ApiResult.ok(convertToDTO(config), "更新成功");
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        GithubRepoConfig config = githubSyncService.getRepoConfigById(id);
        if (config == null) {
            return ApiResult.error(404, "仓库配置不存在");
        }
        githubSyncService.deleteRepoConfig(id);
        return ApiResult.noContent("删除成功");
    }

    @PostMapping("/test-connection")
    public ApiResult<Boolean> testConnection(@RequestBody GithubRepoConfigDTO dto) {
        boolean connected = githubClient.testConnection(
            dto.getRepoUrl(), dto.getBranch(), dto.getAccessToken());
        return ApiResult.ok(connected);
    }

    private GithubRepoConfigDTO convertToDTO(GithubRepoConfig config) {
        GithubRepoConfigDTO dto = new GithubRepoConfigDTO();
        dto.setId(config.getId());
        dto.setName(config.getName());
        dto.setRepoUrl(config.getRepoUrl());
        dto.setBranch(config.getBranch());
        dto.setDocRootPath(config.getDocRootPath());
        dto.setDefaultCategory(config.getDefaultCategory());
        dto.setDefaultTags(config.getDefaultTags());
        dto.setEnabled(config.getEnabled());
        dto.setDescription(config.getDescription());
        return dto;
    }
}
