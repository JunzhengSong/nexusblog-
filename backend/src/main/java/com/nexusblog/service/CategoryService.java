package com.nexusblog.service;

import com.nexusblog.dto.CategoryDTO;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.Category;
import com.nexusblog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DtoMapper dtoMapper;

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return dtoMapper.toCategoryDTOList(categories);
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new com.nexusblog.exception.ResourceNotFoundException("Category", id));
        return dtoMapper.toCategoryDTO(category);
    }
}
