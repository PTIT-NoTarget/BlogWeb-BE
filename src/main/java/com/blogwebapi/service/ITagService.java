package com.blogwebapi.service;

import com.blogwebapi.dto.dto.TagDto;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.entity.Tag;

import java.util.List;

public interface ITagService {
    List<TagDto> viewAllTags();
    BaseResponse addTag(Tag tag);
    BaseResponse findTagByName(String name);
    Tag getTagByName(String tagName);
}
