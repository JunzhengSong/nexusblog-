package com.nexusblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusblog.entity.GithubRepoConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * GitHub仓库配置 Mapper 接口
 */
@Mapper
public interface GithubRepoConfigMapper extends BaseMapper<GithubRepoConfig> {
}
