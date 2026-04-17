package com.nexusblog.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexusblog.entity.Article;
import com.nexusblog.entity.Category;
import com.nexusblog.entity.Tag;
import com.nexusblog.entity.User;
import com.nexusblog.mapper.ArticleMapper;
import com.nexusblog.mapper.CategoryMapper;
import com.nexusblog.mapper.TagMapper;
import com.nexusblog.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final ArticleMapper articleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Create default admin user if not exists
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username", "admin");
        User admin = userMapper.selectOne(userWrapper);

        if (admin == null) {
            admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            admin.setCreateTime(LocalDateTime.now());
            admin.setUpdateTime(LocalDateTime.now());
            admin.setDeleted(0);
            userMapper.insert(admin);
        }

        // Create default categories if not exist
        QueryWrapper<Category> techWrapper = new QueryWrapper<>();
        techWrapper.eq("name", "Technology");
        Category techCategory = categoryMapper.selectOne(techWrapper);

        if (techCategory == null) {
            techCategory = Category.builder()
                    .name("Technology")
                    .build();
            techCategory.setCreateTime(LocalDateTime.now());
            techCategory.setUpdateTime(LocalDateTime.now());
            techCategory.setDeleted(0);
            categoryMapper.insert(techCategory);
        }

        QueryWrapper<Category> lifeWrapper = new QueryWrapper<>();
        lifeWrapper.eq("name", "Life");
        Category lifeCategory = categoryMapper.selectOne(lifeWrapper);

        if (lifeCategory == null) {
            lifeCategory = Category.builder()
                    .name("Life")
                    .build();
            lifeCategory.setCreateTime(LocalDateTime.now());
            lifeCategory.setUpdateTime(LocalDateTime.now());
            lifeCategory.setDeleted(0);
            categoryMapper.insert(lifeCategory);
        }

        // Create default tags if not exist
        QueryWrapper<Tag> javaWrapper = new QueryWrapper<>();
        javaWrapper.eq("name", "Java");
        Tag javaTag = tagMapper.selectOne(javaWrapper);

        if (javaTag == null) {
            javaTag = Tag.builder()
                    .name("Java")
                    .build();
            javaTag.setCreateTime(LocalDateTime.now());
            javaTag.setUpdateTime(LocalDateTime.now());
            javaTag.setDeleted(0);
            tagMapper.insert(javaTag);
        }

        QueryWrapper<Tag> springWrapper = new QueryWrapper<>();
        springWrapper.eq("name", "Spring");
        Tag springTag = tagMapper.selectOne(springWrapper);

        if (springTag == null) {
            springTag = Tag.builder()
                    .name("Spring")
                    .build();
            springTag.setCreateTime(LocalDateTime.now());
            springTag.setUpdateTime(LocalDateTime.now());
            springTag.setDeleted(0);
            tagMapper.insert(springTag);
        }

        QueryWrapper<Tag> vueWrapper = new QueryWrapper<>();
        vueWrapper.eq("name", "Vue");
        Tag vueTag = tagMapper.selectOne(vueWrapper);

        if (vueTag == null) {
            vueTag = Tag.builder()
                    .name("Vue")
                    .build();
            vueTag.setCreateTime(LocalDateTime.now());
            vueTag.setUpdateTime(LocalDateTime.now());
            vueTag.setDeleted(0);
            tagMapper.insert(vueTag);
        }

        // Create welcome article if not exists
        Long articleCount = articleMapper.selectCount(null);
        if (articleCount == 0) {
            Article welcomeArticle = Article.builder()
                    .title("Welcome to NexusBlog")
                    .content("# Welcome to NexusBlog!\n\n" +
                            "This is your new AI-native minimalist personal blog system.\n\n" +
                            "## Features\n\n" +
                            "- Simple and clean design\n" +
                            "- Markdown support\n" +
                            "- Categories and tags\n" +
                            "- Easy to use admin panel\n" +
                            "Get started by logging in to the admin panel and creating your first post!")
                    .categoryId(techCategory.getId())
                    .category(techCategory)
                    .tags(javaList(javaTag, springTag))
                    .build();
            welcomeArticle.setCreateTime(LocalDateTime.now());
            welcomeArticle.setUpdateTime(LocalDateTime.now());
            welcomeArticle.setDeleted(0);
            articleMapper.insert(welcomeArticle);
        }
    }

    private java.util.List<Tag> javaList(Tag tag1, Tag tag2) {
        return java.util.List.of(tag1, tag2);
    }
}
