package com.blogwebapi.service;

import com.blogwebapi.dto.dto.PostDto;
import com.blogwebapi.dto.request.PostReactionRequest;
import com.blogwebapi.dto.request.PostRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.ListpostResponse;
import com.blogwebapi.dto.response.PostReactionResponse;

import java.util.List;

public interface IPostService {
    List<PostDto> viewAllPosts();

    PostDto viewPost(int postId);

    BaseResponse uploadPost(PostRequest postRequest, String accessToken);

    BaseResponse deletePost(int postId, String accessToken);

    BaseResponse updatePost(int postId, PostRequest postRequest, String accessToken);

    PostReactionResponse getReaction(int id);

    BaseResponse checkReaction(int id, String accessToken);

    BaseResponse countView(int id);

    List<PostDto> get5MostViewPosts();

    ListpostResponse viewPostsByPage(int page, int pageSize);

    BaseResponse reaction(int id, String accessToken, PostReactionRequest postReactionRequest);
}
