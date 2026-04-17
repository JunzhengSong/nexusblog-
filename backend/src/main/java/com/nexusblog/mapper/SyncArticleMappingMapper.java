package com.nexusblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusblog.entity.SyncArticleMapping;
import org.apache.ibatis.annotations.Mapper;

/**
 * 同步文章映射 Mapper 接口
 */
@Mapper
public interface SyncArticleMappingMapper extends BaseMapper<SyncArticleMapping> {
}
