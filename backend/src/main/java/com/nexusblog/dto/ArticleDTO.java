package com.nexusblog.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {

    private Long id;
    private String title;
    private String content;
    private CategoryDTO category;
    private List<TagDTO> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
