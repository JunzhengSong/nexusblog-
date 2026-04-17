package com.nexusblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusblog.entity.common.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章实体
 */
@TableName("articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private Long categoryId;

    @TableField(exist = false)
    private Category category;

    @Builder.Default
    @TableField(exist = false)
    private List<Tag> tags = new ArrayList<>();
}
