package com.nexusblog.controller;

import com.nexusblog.client.GithubClient;
import com.nexusblog.dto.GithubRepoConfigDTO;
import com.nexusblog.entity.GithubRepoConfig;
import com.nexusblog.service.GithubSyncService;
import com.nexusblog.util.AesEncryptUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    /**
     * 获取所有仓库配置
     */
    @GetMapping
    public ResponseEntity<List<GithubRepoConfigDTO>> getAll() {
        List<GithubRepoConfig> configs = githubSyncService.getAllRepoConfigs();
        List<GithubRepoConfigDTO> dtos = configs.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * 根据ID获取仓库配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<GithubRepoConfigDTO> getById(@PathVariable Long id) {
        GithubRepoConfig config = githubSyncService.getRepoConfigById(id);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(config));
    }

    /**
     * 创建仓库配置
     */
    @PostMapping
    public ResponseEntity<GithubRepoConfigDTO> create(@Valid @RequestBody GithubRepoConfigDTO dto) {
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

        // 加密访问令牌
        if (dto.getAccessToken() != null && !dto.getAccessToken().isEmpty()) {
            config.setAccessToken(AesEncryptUtil.encrypt(dto.getAccessToken()));
        }

        githubSyncService.createRepoConfig(config);
        return ResponseEntity.ok(convertToDTO(config));
    }

    /**
     * 更新仓库配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<GithubRepoConfigDTO> update(@PathVariable Long id, @Valid @RequestBody GithubRepoConfigDTO dto) {
        GithubRepoConfig config = githubSyncService.getRepoConfigById(id);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }

        config.setName(dto.getName());
        config.setRepoUrl(dto.getRepoUrl());
        config.setBranch(dto.getBranch());
        config.setDocRootPath(dto.getDocRootPath());
        config.setDefaultCategory(dto.getDefaultCategory());
        config.setDefaultTags(dto.getDefaultTags());
        config.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
        config.setDescription(dto.getDescription());

        // 如果accessToken不为空，更新并加密
        if (dto.getAccessToken() != null && !dto.getAccessToken().isEmpty()) {
            config.setAccessToken(AesEncryptUtil.encrypt(dto.getAccessToken()));
        }

        githubSyncService.updateRepoConfig(config);
        return ResponseEntity.ok(convertToDTO(config));
    }

    /**
     * 删除仓库配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        GithubRepoConfig config = githubSyncService.getRepoConfigById(id);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        githubSyncService.deleteRepoConfig(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 测试仓库连接
     */
    @PostMapping("/test-connection")
    public ResponseEntity<Boolean> testConnection(@RequestBody GithubRepoConfigDTO dto) {
        boolean connected = githubClient.testConnection(
            dto.getRepoUrl(), dto.getBranch(), dto.getAccessToken());
        return ResponseEntity.ok(connected);
    }

    /**
     * 转换实体为DTO，不返回accessToken
     */
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
        // 不返回accessToken
        return dto;
    }
}
