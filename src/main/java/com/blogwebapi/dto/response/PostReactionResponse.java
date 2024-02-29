package com.blogwebapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReactionResponse extends BaseResponse{
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private int commentCount;
}
