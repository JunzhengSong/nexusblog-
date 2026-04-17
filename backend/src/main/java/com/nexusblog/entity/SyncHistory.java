package com.nexusblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusblog.entity.common.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 同步历史实体
 */
@TableName("sync_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncHistory extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long repoConfigId;

    private GithubRepoConfig repoConfig;

    private String status; // 同步状态：PENDING, RUNNING, SUCCESS, FAILED

    private Integer totalFiles; // 总文件数

    private Integer successCount; // 成功数

    private Integer failCount; // 失败数

    private String errorMessage; // 错误信息

    private LocalDateTime startTime; // 开始时间

    private LocalDateTime endTime; // 结束时间
}
