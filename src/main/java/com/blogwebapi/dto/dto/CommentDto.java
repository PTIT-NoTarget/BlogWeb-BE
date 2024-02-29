package com.blogwebapi.dto.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentDto {
    private int commentId;
    private UserDto user;
    private String content;
    private Timestamp createdDate;
    private int likeCount;
    private int dislikeCount;
    private int parentCommentId;
    private int commentLevel;
}
