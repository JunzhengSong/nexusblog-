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
 * 标签实体
 */
@TableName("tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @Builder.Default
    @TableField(exist = false)
    private List<Article> articles = new ArrayList<>();
}
