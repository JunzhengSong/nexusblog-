-- 添加 update_time 列到 sync_history 表
-- 修复 MyBatis-Plus 自动填充功能所需的字段缺失问题

ALTER TABLE sync_history
ADD COLUMN update_time DATETIME DEFAULT NULL COMMENT '更新时间'
AFTER repo_config_id;
