package com.blogwebapi.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentReactionRequest {
    private String reactType;
    private int postId;
}