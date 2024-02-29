package com.blogwebapi.service.impl;

import com.blogwebapi.dto.dto.TagDto;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.entity.Tag;
import com.blogwebapi.repository.ITagRepository;
import com.blogwebapi.service.ITagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService implements ITagService {
    @Autowired
    private ITagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TagDto> viewAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).toList();
    }

    @Override
    public BaseResponse addTag(Tag tag) {
        tagRepository.save(tag);
        return new BaseResponse(200, "Tag Added Successfully");
    }

    @Override
    public BaseResponse findTagByName(String name) {
        Tag tag = tagRepository.findByName(name);
        if(tag == null) return new BaseResponse(404, "Tag Not Found");
        return new BaseResponse(200, "Tag Founded");
    }

    @Override
    public Tag getTagByName(String tagName) {
        return tagRepository.findByName(tagName);
    }
}
