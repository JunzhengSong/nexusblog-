package com.nexusblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GithubRepoConfigDTO {

    private Long id;

    @NotBlank(message = "配置名称不能为空")
    private String name;

    @NotBlank(message = "仓库地址不能为空")
    private String repoUrl;

    @NotBlank(message = "分支名称不能为空")
    private String branch;

    @NotBlank(message = "文档根目录不能为空")
    private String docRootPath;

    private String accessToken; // 明文，前端传入

    private String defaultCategory;

    private String defaultTags;

    private Boolean enabled;

    private String description;
}
