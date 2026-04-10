package com.nexusblog.config;

import com.nexusblog.entity.Article;
import com.nexusblog.entity.Category;
import com.nexusblog.entity.Tag;
import com.nexusblog.entity.User;
import com.nexusblog.repository.ArticleRepository;
import com.nexusblog.repository.CategoryRepository;
import com.nexusblog.repository.TagRepository;
import com.nexusblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Create default admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
        }

        // Create default categories if not exist
        Category techCategory = categoryRepository.findByName("Technology")
                .orElseGet(() -> categoryRepository.save(
                        Category.builder().name("Technology").build()));

        Category lifeCategory = categoryRepository.findByName("Life")
                .orElseGet(() -> categoryRepository.save(
                        Category.builder().name("Life").build()));

        // Create default tags if not exist
        Tag javaTag = tagRepository.findByName("Java")
                .orElseGet(() -> tagRepository.save(
                        Tag.builder().name("Java").build()));

        Tag springTag = tagRepository.findByName("Spring")
                .orElseGet(() -> tagRepository.save(
                        Tag.builder().name("Spring").build()));

        Tag vueTag = tagRepository.findByName("Vue")
                .orElseGet(() -> tagRepository.save(
                        Tag.builder().name("Vue").build()));

        // Create welcome article if not exists
        if (articleRepository.count() == 0) {
            Article welcomeArticle = Article.builder()
                    .title("Welcome to NexusBlog")
                    .content("# Welcome to NexusBlog!\n\n" +
                            "This is your new AI-native minimalist personal blog system.\n\n" +
                            "## Features\n\n" +
                            "- Simple and clean design\n" +
                            "- Markdown support\n" +
                            "- Categories and tags\n" +
                            "- Easy to use admin panel\n\n" +
                            "Get started by logging in to the admin panel and creating your first post!")
                    .category(techCategory)
                    .tags(javaList(javaTag, springTag))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            articleRepository.save(welcomeArticle);
        }
    }

    private java.util.List<Tag> javaList(Tag tag1, Tag tag2) {
        return java.util.List.of(tag1, tag2);
    }
}
