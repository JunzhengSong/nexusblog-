-- NexusBlog 数据库初始化脚本
-- 使用 UTF8MB4 字符集以支持 emoji 等特殊字符

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 标签表
CREATE TABLE IF NOT EXISTS `tags` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 文章表
CREATE TABLE IF NOT EXISTS `articles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `content` TEXT COMMENT '文章内容',
    `category_id` BIGINT COMMENT '分类ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_articles_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 文章-标签关联表
CREATE TABLE IF NOT EXISTS `article_tags` (
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`article_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`),
    CONSTRAINT `fk_article_tags_article` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_article_tags_tag` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章-标签关联表';

-- GitHub仓库配置表
CREATE TABLE IF NOT EXISTS `github_repo_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `name` VARCHAR(200) NOT NULL COMMENT '配置名称',
    `repo_url` VARCHAR(500) NOT NULL COMMENT 'GitHub仓库地址',
    `branch` VARCHAR(100) NOT NULL COMMENT '分支名称',
    `doc_root_path` VARCHAR(200) NOT NULL COMMENT '文档根目录',
    `access_token` VARCHAR(200) COMMENT '加密后的访问令牌',
    `default_category` VARCHAR(100) COMMENT '默认分类',
    `default_tags` VARCHAR(200) COMMENT '默认标签，逗号分隔',
    `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    `description` VARCHAR(500) COMMENT '描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_enabled` (`enabled`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GitHub仓库配置表';

-- 同步文章映射表
CREATE TABLE IF NOT EXISTS `sync_article_mapping` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '映射ID',
    `repo_config_id` BIGINT NOT NULL COMMENT '仓库配置ID',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `file_path` VARCHAR(500) NOT NULL COMMENT 'GitHub文件路径',
    `file_hash` VARCHAR(100) NOT NULL COMMENT '文件内容SHA哈希值',
    `file_name` VARCHAR(200) NOT NULL COMMENT '文件名',
    `last_commit_sha` VARCHAR(100) COMMENT '最后提交的SHA',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_repo_file` (`repo_config_id`, `file_path`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_repo_config_id` (`repo_config_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_sync_mapping_repo_config` FOREIGN KEY (`repo_config_id`) REFERENCES `github_repo_config` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sync_mapping_article` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='同步文章映射表';

-- 同步历史表
CREATE TABLE IF NOT EXISTS `sync_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '历史ID',
    `repo_config_id` BIGINT NOT NULL COMMENT '仓库配置ID',
    `status` VARCHAR(50) NOT NULL COMMENT '同步状态：PENDING, RUNNING, SUCCESS, FAILED',
    `total_files` INT NOT NULL DEFAULT 0 COMMENT '总文件数',
    `success_count` INT NOT NULL DEFAULT 0 COMMENT '成功数',
    `fail_count` INT NOT NULL DEFAULT 0 COMMENT '失败数',
    `error_message` TEXT COMMENT '错误信息',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_repo_config_id` (`repo_config_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='同步历史表';
