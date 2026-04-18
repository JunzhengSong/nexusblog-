package com.nexusblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusblog.entity.common.BaseEntity;
import lombok.*;

/**
 * 同步文章映射实体
 */
@TableName("sync_article_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncArticleMapping extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long repoConfigId;

    @TableField(exist = false)
    private GithubRepoConfig repoConfig;

    private Long articleId;

    @TableField(exist = false)
    private Article article;

    private String filePath; // GitHub文件路径

    private String fileHash; // 文件内容SHA哈希值

    private String fileName; // 文件名

    private String lastCommitSha; // 最后提交的SHA
}
