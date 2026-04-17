package com.nexusblog.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class GithubClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String GITHUB_API_BASE = "https://api.github.com";
    private static final int API_TIMEOUT = 10000; // 10 seconds

    public GithubClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 获取指定目录下的所有Markdown文件
     * @param repoUrl GitHub仓库地址，如https://github.com/username/repo
     * @param branch 分支名称
     * @param path 目录路径
     * @param accessToken 访问令牌（可为null）
     * @return 文件列表，包含path、sha、download_url等信息
     */
    public List<GithubFile> listMarkdownFiles(String repoUrl, String branch, String path, String accessToken) {
        List<GithubFile> markdownFiles = new ArrayList<>();

        try {
            // 解析仓库地址，提取owner和repo名称
            String[] repoParts = parseRepoUrl(repoUrl);
            String owner = repoParts[0];
            String repo = repoParts[1];

            // 构造API URL
            String apiUrl = String.format("%s/repos/%s/%s/contents/%s?ref=%s",
                GITHUB_API_BASE, owner, repo, path, branch);

            // 创建请求头
            HttpHeaders headers = createHeaders(accessToken);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                if (root.isArray()) {
                    for (JsonNode fileNode : root) {
                        String type = fileNode.get("type").asText();
                        String fileName = fileNode.get("name").asText();

                        if ("file".equals(type) && fileName.endsWith(".md")) {
                            GithubFile file = new GithubFile();
                            file.setPath(fileNode.get("path").asText());
                            file.setName(fileName);
                            file.setSha(fileNode.get("sha").asText());
                            file.setDownloadUrl(fileNode.get("download_url").asText());
                            file.setSize(fileNode.get("size").asInt());
                            markdownFiles.add(file);
                        } else if ("dir".equals(type)) {
                            // 递归遍历子目录
                            List<GithubFile> subDirFiles = listMarkdownFiles(
                                repoUrl, branch, fileNode.get("path").asText(), accessToken);
                            markdownFiles.addAll(subDirFiles);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取GitHub文件列表失败: " + e.getMessage(), e);
        }

        return markdownFiles;
    }

    /**
     * 获取文件内容
     * @param downloadUrl 文件的download_url
     * @param accessToken 访问令牌（可为null）
     * @return 文件内容
     */
    public String getFileContent(String downloadUrl, String accessToken) {
        try {
            HttpHeaders headers = createHeaders(accessToken);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                downloadUrl, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 检查是否是Base64编码的内容（有些GitHub API返回Base64）
                try {
                    byte[] decoded = Base64.getDecoder().decode(response.getBody().trim());
                    return new String(decoded, StandardCharsets.UTF_8);
                } catch (IllegalArgumentException e) {
                    // 不是Base64，直接返回
                    return response.getBody();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("获取GitHub文件内容失败: " + e.getMessage(), e);
        }
    }

    /**
     * 测试仓库连接
     * @param repoUrl GitHub仓库地址
     * @param branch 分支名称
     * @param accessToken 访问令牌（可为null）
     * @return 是否连接成功
     */
    public boolean testConnection(String repoUrl, String branch, String accessToken) {
        try {
            String[] repoParts = parseRepoUrl(repoUrl);
            String owner = repoParts[0];
            String repo = repoParts[1];

            String apiUrl = String.format("%s/repos/%s/%s/branches/%s",
                GITHUB_API_BASE, owner, repo, branch);

            HttpHeaders headers = createHeaders(accessToken);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl, HttpMethod.GET, request, String.class);

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析GitHub仓库地址，提取owner和repo名称
     * @param repoUrl 仓库地址，如https://github.com/username/repo
     * @return [owner, repo]
     */
    private String[] parseRepoUrl(String repoUrl) {
        // 移除后缀.git
        if (repoUrl.endsWith(".git")) {
            repoUrl = repoUrl.substring(0, repoUrl.length() - 4);
        }

        // 移除前缀https://github.com/或http://github.com/
        String prefix = "github.com/";
        int index = repoUrl.indexOf(prefix);
        if (index == -1) {
            throw new IllegalArgumentException("无效的GitHub仓库地址");
        }

        String path = repoUrl.substring(index + prefix.length());
        String[] parts = path.split("/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("无效的GitHub仓库地址");
        }

        return new String[]{parts[0], parts[1]};
    }

    /**
     * 创建请求头
     */
    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "NexusBlog-GitHub-Sync");

        if (accessToken != null && !accessToken.isEmpty()) {
            headers.set("Authorization", "token " + accessToken);
        }

        return headers;
    }

    /**
     * GitHub文件信息DTO
     */
    public static class GithubFile {
        private String path;
        private String name;
        private String sha;
        private String downloadUrl;
        private Integer size;

        // Getters and Setters
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSha() { return sha; }
        public void setSha(String sha) { this.sha = sha; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
        public Integer getSize() { return size; }
        public void setSize(Integer size) { this.size = size; }
    }
}
