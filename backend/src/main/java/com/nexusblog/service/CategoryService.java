package com.nexusblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexusblog.dto.CategoryDTO;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.Category;
import com.nexusblog.exception.ResourceNotFoundException;
import com.nexusblog.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final DtoMapper dtoMapper;

    public List<CategoryDTO> getAllCategories() {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Category> categories = categoryMapper.selectList(wrapper);
        return dtoMapper.toCategoryDTOList(categories);
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new ResourceNotFoundException("Category", id);
        }
        return dtoMapper.toCategoryDTO(category);
    }

    /**
     * 根据名称查找分类
     */
    public Category findByName(String name) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        return categoryMapper.selectOne(wrapper);
    }
}
