package com.nexusblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nexusblog.entity.SyncHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 同步历史 Mapper 接口
 */
@Mapper
public interface SyncHistoryMapper extends BaseMapper<SyncHistory> {
}
