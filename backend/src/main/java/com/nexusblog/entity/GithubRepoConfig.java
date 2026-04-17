package com.nexusblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusblog.entity.common.BaseEntity;
import lombok.*;

/**
 * GitHub仓库配置实体
 */
@TableName("github_repo_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubRepoConfig extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name; // 配置名称

    private String repoUrl; // GitHub仓库地址

    private String branch; // 分支名称

    private String docRootPath; // 文档根目录

    private String accessToken; // 加密后的访问令牌

    private String defaultCategory; // 默认分类

    private String defaultTags; // 默认标签，逗号分隔

    private Boolean enabled; // 是否启用

    private String description; // 描述
}
