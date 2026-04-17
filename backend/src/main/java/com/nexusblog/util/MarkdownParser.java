package com.nexusblog.util;

import org.yaml.snakeyaml.Yaml;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownParser {

    private static final Pattern FRONT_MATTER_PATTERN = Pattern.compile(
        "^---\\s*\\n(.*?)\\n---\\s*\\n", Pattern.DOTALL);

    private static final Pattern DATE_IN_FILENAME_PATTERN = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2})-(.*)$");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 解析Markdown内容，提取元数据和正文
     * @param content Markdown内容
     * @param fileName 文件名
     * @param defaultCategory 默认分类
     * @param defaultTags 默认标签
     * @return 解析结果
     */
    public static MarkdownContent parse(String content, String fileName, String defaultCategory, String defaultTags) {
        MarkdownContent result = new MarkdownContent();
        String body = content;

        // 解析YAML Front Matter
        Matcher matcher = FRONT_MATTER_PATTERN.matcher(content);
        if (matcher.find()) {
            String frontMatterStr = matcher.group(1);
            Yaml yaml = new Yaml();
            Map<String, Object> frontMatter = yaml.load(frontMatterStr);

            // 提取元数据
            if (frontMatter != null) {
                result.setTitle(getStringValue(frontMatter, "title"));
                result.setDate(getDateValue(frontMatter, "date"));
                result.setCategory(getStringValue(frontMatter, "category"));
                result.setTags(getTagsValue(frontMatter, "tags"));
                result.setExcerpt(getStringValue(frontMatter, "excerpt"));
            }

            // 移除Front Matter，得到正文
            body = content.substring(matcher.end());
        }

        result.setContent(body);

        // 如果没有从Front Matter获取到标题，从文件名提取
        if (result.getTitle() == null || result.getTitle().isEmpty()) {
            String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
            Matcher dateMatcher = DATE_IN_FILENAME_PATTERN.matcher(nameWithoutExt);
            if (dateMatcher.find()) {
                // 文件名包含日期，如2024-01-01-文章标题.md
                result.setTitle(dateMatcher.group(2).replaceAll("-", " "));
                if (result.getDate() == null) {
                    try {
                        result.setDate(LocalDate.parse(dateMatcher.group(1), DATE_FORMATTER));
                    } catch (Exception e) {
                        // 日期解析失败，忽略
                    }
                }
            } else {
                // 文件名不包含日期
                result.setTitle(nameWithoutExt.replaceAll("-", " "));
            }
        }

        // 如果没有分类，使用默认分类
        if (result.getCategory() == null || result.getCategory().isEmpty()) {
            result.setCategory(defaultCategory != null ? defaultCategory : "未分类");
        }

        // 如果没有标签，使用默认标签
        if (result.getTags() == null || result.getTags().isEmpty()) {
            if (defaultTags != null && !defaultTags.isEmpty()) {
                result.setTags(Arrays.asList(defaultTags.split(",")));
            } else {
                result.setTags(Collections.emptyList());
            }
        }

        // 如果没有摘要，自动提取正文前200个字符
        if (result.getExcerpt() == null || result.getExcerpt().isEmpty()) {
            String plainText = body.replaceAll("#|\\*|`|!\\[.*?\\]\\(.*?\\)|\\[.*?\\]\\(.*?\\)", "")
                .replaceAll("\\s+", " ").trim();
            if (plainText.length() > 200) {
                result.setExcerpt(plainText.substring(0, 200) + "...");
            } else {
                result.setExcerpt(plainText);
            }
        }

        return result;
    }

    private static String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString().trim() : null;
    }

    private static LocalDate getDateValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof LocalDate) {
                return (LocalDate) value;
            } else {
                return LocalDate.parse(value.toString().trim(), DATE_FORMATTER);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<String> getTagsValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof List) {
            return (List<String>) value;
        } else if (value instanceof String) {
            String tagsStr = (String) value;
            return Arrays.asList(tagsStr.split(","));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Markdown解析结果DTO
     */
    public static class MarkdownContent {
        private String title;
        private LocalDate date;
        private String category;
        private List<String> tags;
        private String excerpt;
        private String content;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public String getExcerpt() { return excerpt; }
        public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
