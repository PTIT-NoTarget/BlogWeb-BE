package com.blogwebapi.dto.dto;

import com.blogwebapi.entity.constants.PostStatus;
import com.blogwebapi.entity.Tag;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostDto {
    private int postId;
    private UserDto user;
    private String title;
    private String content;
    private String thumbnail;
    private Timestamp dateCreated;
    private int viewCount;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private PostStatus status;
    private List<TagDto> tags;
}
