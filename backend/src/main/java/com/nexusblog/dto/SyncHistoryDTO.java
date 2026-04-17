package com.nexusblog.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 同步历史 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncHistoryDTO {
    private Long id;
    private Long repoConfigId;
    private String status;
    private Integer totalFiles;
    private Integer successCount;
    private Integer failCount;
    private String errorMessage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}
