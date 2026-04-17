package com.nexusblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nexusblog.client.GithubClient;
import com.nexusblog.dto.SyncHistoryDTO;
import com.nexusblog.entity.*;
import com.nexusblog.mapper.*;
import com.nexusblog.util.AesEncryptUtil;
import com.nexusblog.util.MarkdownParser;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubSyncService {

    private final GithubClient githubClient;
    private final GithubRepoConfigMapper repoConfigMapper;
    private final SyncHistoryMapper syncHistoryMapper;
    private final SyncArticleMappingMapper syncArticleMappingMapper;
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;

    /**
     * 获取所有仓库配置（按创建时间降序）
     */
    public List<GithubRepoConfig> getAllRepoConfigs() {
        QueryWrapper<GithubRepoConfig> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return repoConfigMapper.selectList(wrapper);
    }

    /**
     * 获取启用的仓库配置
     */
    public List<GithubRepoConfig> getEnabledRepoConfigs() {
        QueryWrapper<GithubRepoConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("enabled", true);
        wrapper.orderByDesc("create_time");
        return repoConfigMapper.selectList(wrapper);
    }

    /**
     * 获取仓库配置
     */
    public GithubRepoConfig getRepoConfigById(Long id) {
        return repoConfigMapper.selectById(id);
    }

    /**
     * 创建仓库配置
     */
    public void createRepoConfig(GithubRepoConfig config) {
        repoConfigMapper.insert(config);
    }

    /**
     * 更新仓库配置
     */
    public void updateRepoConfig(GithubRepoConfig config) {
        repoConfigMapper.updateById(config);
    }

    /**
     * 删除仓库配置
     */
    public void deleteRepoConfig(Long id) {
        repoConfigMapper.deleteById(id);
    }

    /**
     * 获取同步历史记录（分页）
     */
    public List<SyncHistoryDTO> getSyncHistoryPage(int page, int size) {
        Page<SyncHistory> pageParam = new Page<>(page, size);
        QueryWrapper<SyncHistory> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        Page<SyncHistory> result = syncHistoryMapper.selectPage(pageParam, wrapper);

        List<SyncHistoryDTO> dtos = new ArrayList<>();
        for (SyncHistory history : result.getRecords()) {
            SyncHistoryDTO dto = SyncHistoryDTO.builder()
                    .id(history.getId())
                    .repoConfigId(history.getRepoConfigId())
                    .status(history.getStatus())
                    .totalFiles(history.getTotalFiles())
                    .successCount(history.getSuccessCount())
                    .failCount(history.getFailCount())
                    .errorMessage(history.getErrorMessage())
                    .startTime(history.getStartTime())
                    .endTime(history.getEndTime())
                    .createTime(history.getCreateTime())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 获取仓库配置的同步历史
     */
    public List<SyncHistory> getSyncHistoryByRepoConfigId(Long repoConfigId) {
        QueryWrapper<SyncHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("repo_config_id", repoConfigId);
        wrapper.orderByDesc("create_time");
        return syncHistoryMapper.selectList(wrapper);
    }

    /**
     * 根据ID获取同步历史
     */
    public SyncHistory getSyncHistoryById(Long syncHistoryId) {
        return syncHistoryMapper.selectById(syncHistoryId);
    }

    /**
     * 删除同步文章（同时删除映射关系和文章）
     */
    @Transactional
    public void deleteSyncArticle(Long articleId) {
        // 删除映射关系
        QueryWrapper<SyncArticleMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId);
        syncArticleMappingMapper.delete(wrapper);

        // 删除文章
        articleMapper.deleteById(articleId);
    }

    /**
     * 获取同步文章映射（分页）
     */
    public List<SyncArticleMapping> getSyncArticleMappingPage(int page, int size) {
        Page<SyncArticleMapping> pageParam = new Page<>(page, size);
        QueryWrapper<SyncArticleMapping> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        Page<SyncArticleMapping> result = syncArticleMappingMapper.selectPage(pageParam, wrapper);
        return result.getRecords();
    }

    /**
     * 获取仓库配置的同步文章映射
     */
    public List<SyncArticleMapping> getSyncArticleMappingByRepoConfigId(Long repoConfigId) {
        QueryWrapper<SyncArticleMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("repo_config_id", repoConfigId);
        wrapper.orderByDesc("create_time");
        return syncArticleMappingMapper.selectList(wrapper);
    }

    /**
     * 根据文章ID获取同步映射
     */
    public SyncArticleMapping getSyncArticleMappingByArticleId(Long articleId) {
        QueryWrapper<SyncArticleMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId);
        return syncArticleMappingMapper.selectOne(wrapper);
    }

    /**
     * 删除文章的同步映射
     */
    public void deleteSyncArticleMappingByArticleId(Long articleId) {
        QueryWrapper<SyncArticleMapping> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId);
        syncArticleMappingMapper.delete(wrapper);
    }

    /**
     * 启动同步
     */
    @Transactional
    public Long startSync(Long repoConfigId) {
        GithubRepoConfig repoConfig = repoConfigMapper.selectById(repoConfigId);
        if (repoConfig == null) {
            throw new RuntimeException("仓库配置不存在");
        }

        // 创建同步历史记录
        SyncHistory syncHistory = SyncHistory.builder()
                .repoConfigId(repoConfigId)
                .repoConfig(repoConfig)
                .status("PENDING")
                .totalFiles(0)
                .successCount(0)
                .failCount(0)
                .build();
        syncHistoryMapper.insert(syncHistory);

        // 异步执行同步
        syncRepository(repoConfigId, syncHistory.getId());

        return syncHistory.getId();
    }

    /**
     * 异步执行同步
     */
    @Async("githubSyncExecutor")
    @Transactional
    public void syncRepository(Long repoConfigId, Long syncHistoryId) {
        SyncHistory syncHistory = syncHistoryMapper.selectById(syncHistoryId);
        if (syncHistory == null) {
            throw new RuntimeException("同步记录不存在");
        }

        try {
            GithubRepoConfig repoConfig = repoConfigMapper.selectById(repoConfigId);
            if (repoConfig == null) {
                throw new RuntimeException("仓库配置不存在");
            }

            // 更新同步状态为运行中
            syncHistory.setStatus("RUNNING");
            syncHistory.setStartTime(LocalDateTime.now());
            syncHistoryMapper.updateById(syncHistory);

            // 解密访问令牌
            String accessToken = repoConfig.getAccessToken() != null ?
                AesEncryptUtil.decrypt(repoConfig.getAccessToken()) : null;

            // 获取所有Markdown文件
            List<GithubClient.GithubFile> files = githubClient.listMarkdownFiles(
                repoConfig.getRepoUrl(), repoConfig.getBranch(), repoConfig.getDocRootPath(), accessToken);

            syncHistory.setTotalFiles(files.size());
            syncHistoryMapper.updateById(syncHistory);

            int successCount = 0;
            int failCount = 0;

            for (GithubClient.GithubFile file : files) {
                try {
                    // 获取文件内容
                    String content = githubClient.getFileContent(file.getDownloadUrl(), accessToken);
                    if (content == null) {
                        failCount++;
                        continue;
                    }

                    // 计算文件哈希
                    String fileHash = calculateSHA256(content);

                    // 去重检查
                    QueryWrapper<SyncArticleMapping> wrapper = new QueryWrapper<>();
                    wrapper.eq("repo_config_id", repoConfigId);
                    wrapper.eq("file_path", file.getPath());
                    SyncArticleMapping existingMapping = syncArticleMappingMapper.selectOne(wrapper);

                    if (existingMapping != null) {
                        // 文件内容没有变化，跳过
                        if (existingMapping.getFileHash().equals(fileHash)) {
                            successCount++;
                            continue;
                        }
                        // 文件内容有变化，更新文章
                        Article article = articleMapper.selectById(existingMapping.getArticleId());
                        if (article != null) {
                            updateArticle(article, content, file.getName(), repoConfig);
                            existingMapping.setFileHash(fileHash);
                            existingMapping.setLastCommitSha(file.getSha());
                            syncArticleMappingMapper.updateById(existingMapping);
                        }
                    } else {
                        // 新文件，创建文章
                        Article article = createArticle(content, file.getName(), repoConfig);
                        // 创建映射关系
                        SyncArticleMapping mapping = SyncArticleMapping.builder()
                            .repoConfigId(repoConfigId)
                            .repoConfig(repoConfig)
                            .articleId(article.getId())
                            .article(article)
                            .filePath(file.getPath())
                            .fileName(file.getName())
                            .fileHash(fileHash)
                            .lastCommitSha(file.getSha())
                            .build();
                        syncArticleMappingMapper.insert(mapping);
                    }

                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    e.printStackTrace();
                }

                // 更新进度
                syncHistory.setSuccessCount(successCount);
                syncHistory.setFailCount(failCount);
                syncHistoryMapper.updateById(syncHistory);
            }

            // 同步完成
            syncHistory.setStatus("SUCCESS");
            syncHistory.setEndTime(LocalDateTime.now());
            syncHistoryMapper.updateById(syncHistory);

        } catch (Exception e) {
            // 同步失败
            syncHistory.setStatus("FAILED");
            syncHistory.setErrorMessage(e.getMessage());
            syncHistory.setEndTime(LocalDateTime.now());
            syncHistoryMapper.updateById(syncHistory);
        }
    }

    /**
     * 创建新文章
     */
    private Article createArticle(String content, String fileName, GithubRepoConfig repoConfig) {
        MarkdownParser.MarkdownContent markdownContent = MarkdownParser.parse(
            content, fileName, repoConfig.getDefaultCategory(), repoConfig.getDefaultTags());

        Article article = Article.builder()
                .title(markdownContent.getTitle())
                .content(markdownContent.getContent())
                .build();

        // 设置分类
        Category category = getOrCreateCategory(markdownContent.getCategory());
        article.setCategory(category);
        article.setCategoryId(category.getId());

        // 设置标签
        List<Tag> tags = getOrCreateTags(markdownContent.getTags());
        article.setTags(tags);

        articleMapper.insert(article);
        return article;
    }

    /**
     * 更新现有文章
     */
    private void updateArticle(Article article, String content, String fileName, GithubRepoConfig repoConfig) {
        MarkdownParser.MarkdownContent markdownContent = MarkdownParser.parse(
            content, fileName, repoConfig.getDefaultCategory(), repoConfig.getDefaultTags());

        article.setTitle(markdownContent.getTitle());
        article.setContent(markdownContent.getContent());

        // 更新分类
        Category category = getOrCreateCategory(markdownContent.getCategory());
        article.setCategory(category);
        article.setCategoryId(category.getId());

        // 更新标签
        List<Tag> tags = getOrCreateTags(markdownContent.getTags());
        article.setTags(tags);

        articleMapper.updateById(article);
    }

    /**
     * 获取或创建分类
     */
    private Category getOrCreateCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            categoryName = "未分类";
        }
        final String finalCategoryName = categoryName;

        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("name", finalCategoryName);
        Category category = categoryMapper.selectOne(wrapper);

        if (category != null) {
            return category;
        } else {
            Category newCategory = Category.builder()
                    .name(finalCategoryName)
                    .build();
            categoryMapper.insert(newCategory);
            return newCategory;
        }
    }

    /**
     * 获取或创建标签
     */
    private List<Tag> getOrCreateTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        if (tagNames == null || tagNames.isEmpty()) {
            return tags;
        }

        for (String tagName : tagNames) {
            final String finalTagName = tagName.trim();
            if (finalTagName.isEmpty()) {
                continue;
            }

            QueryWrapper<Tag> wrapper = new QueryWrapper<>();
            wrapper.eq("name", finalTagName);
            Tag tag = tagMapper.selectOne(wrapper);

            if (tag != null) {
                tags.add(tag);
            } else {
                Tag newTag = Tag.builder()
                        .name(finalTagName)
                        .build();
                tagMapper.insert(newTag);
                tags.add(newTag);
            }
        }

        return tags;
    }

    /**
     * 计算字符串的SHA256哈希
     */
    private String calculateSHA256(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("计算哈希失败", e);
        }
    }
}
