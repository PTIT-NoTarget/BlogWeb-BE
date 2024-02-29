package com.blogwebapi.dto.response;

import com.blogwebapi.dto.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListpostResponse extends BaseResponse {
    private int totalPost;
    private int totalPage;
    private List<PostDto> postList;
}
