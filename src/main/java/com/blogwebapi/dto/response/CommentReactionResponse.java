package com.blogwebapi.dto.response;

import com.blogwebapi.dto.dto.CommentDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionResponse extends BaseResponse{
    List<Integer> commentIds;
    List<String> reactTypes;
}
