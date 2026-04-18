package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import com.nexusblog.dto.TagDTO;
import com.nexusblog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ApiResult<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ApiResult.ok(tags);
    }

    @GetMapping("/{id}")
    public ApiResult<TagDTO> getTagById(@PathVariable Long id) {
        TagDTO tag = tagService.getTagById(id);
        return ApiResult.ok(tag);
    }
}
