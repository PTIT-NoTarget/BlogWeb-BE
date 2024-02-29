package com.blogwebapi.service;

import com.blogwebapi.dto.dto.CommentDto;
import com.blogwebapi.dto.request.CommentReactionRequest;
import com.blogwebapi.dto.request.CommentRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.CommentReactionResponse;
import com.blogwebapi.dto.response.PostReactionResponse;

import java.util.List;

public interface ICommentService {
    List<CommentDto> getAllComments(int postId);

    BaseResponse commentPost(int postId, String accessToken, CommentRequest commentRequest);

    PostReactionResponse getReaction(int id);

    CommentReactionResponse checkReaction(int id, String accessToken);

    BaseResponse reaction(int id, String accessToken, CommentReactionRequest commentReactionRequest);
}
