package com.nexusblog.service;

import com.nexusblog.dto.TagDTO;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.Tag;
import com.nexusblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final DtoMapper dtoMapper;

    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return dtoMapper.toTagDTOList(tags);
    }

    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new com.nexusblog.exception.ResourceNotFoundException("Tag", id));
        return dtoMapper.toTagDTO(tag);
    }
}
