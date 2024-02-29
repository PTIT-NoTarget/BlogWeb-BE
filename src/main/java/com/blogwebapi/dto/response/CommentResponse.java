package com.blogwebapi.dto.response;

import com.blogwebapi.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse extends BaseResponse{
    private List<Comment> commentList;
}
