package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import com.nexusblog.dto.CategoryDTO;
import com.nexusblog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResult<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ApiResult.ok(categories);
    }

    @GetMapping("/{id}")
    public ApiResult<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ApiResult.ok(category);
    }
}
