package com.nexusblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexusblog.dto.TagDTO;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.Tag;
import com.nexusblog.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagMapper tagMapper;
    private final DtoMapper dtoMapper;

    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagMapper.selectList(null);
        return dtoMapper.toTagDTOList(tags);
    }

    public TagDTO getTagById(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new com.nexusblog.exception.ResourceNotFoundException("Tag", id);
        }
        return dtoMapper.toTagDTO(tag);
    }

    /**
     * 根据名称查找标签
     */
    public Tag findByName(String name) {
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        return tagMapper.selectOne(wrapper);
    }
}
