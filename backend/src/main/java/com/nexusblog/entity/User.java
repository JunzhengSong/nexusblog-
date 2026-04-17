package com.nexusblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nexusblog.entity.common.BaseEntity;
import lombok.*;

/**
 * 用户实体
 */
@TableName("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;
}
