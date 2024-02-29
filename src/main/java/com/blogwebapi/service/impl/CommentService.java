package com.blogwebapi.service.impl;

import com.blogwebapi.dto.dto.CommentDto;
import com.blogwebapi.dto.request.CommentReactionRequest;
import com.blogwebapi.dto.request.CommentRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.CommentReactionResponse;
import com.blogwebapi.dto.response.PostReactionResponse;
import com.blogwebapi.entity.Comment;
import com.blogwebapi.entity.Post;
import com.blogwebapi.entity.Reaction;
import com.blogwebapi.entity.User;
import com.blogwebapi.entity.constants.ReactionType;
import com.blogwebapi.repository.ICommentRepository;
import com.blogwebapi.repository.IPostRepository;
import com.blogwebapi.repository.IReactionRepository;
import com.blogwebapi.service.ICommentService;
import com.blogwebapi.service.IJWTService;
import com.blogwebapi.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IJWTService jwtService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IReactionRepository reactionRepository;

    private List<Comment> solveCommentListToResponse(List<Comment> comments) {
        List<Comment> result = new ArrayList<>();
        Stack<Comment> stack = new Stack<>();
        Map<Comment, Integer> map = new HashMap<>();
        for (Comment comment : comments) {
            if (comment.getParentCommentId() == 0) {
                stack.push(comment);
                map.put(comment, 1);
            }
        }
        while (!stack.isEmpty()) {
            Comment comment = stack.pop();
            result.add(comment);
            for (Comment c : comments) {
                if (c.getParentCommentId() == comment.getCommentId() && !map.containsKey(c)) {
                    stack.push(c);
                    map.put(c, 1);
                }
            }
        }
        return result;
    }

    @Override
    public List<CommentDto> getAllComments(int postId) {
        List<Comment> comments = commentRepository.findAllByPostPostId(postId);
        return solveCommentListToResponse(comments).stream().map(comment -> modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
    }

    @Override
    public BaseResponse commentPost(int postId, String accessToken, CommentRequest commentRequest) {
        if (accessToken == null) {
            return new BaseResponse(401, "Unauthorized");
        }
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            Post post = postRepository.findById(postId).get();
            Comment comment = new Comment();
            comment.setPost(post);
            comment.setUser(user);
            comment.setContent(commentRequest.getContent());
            comment.setDateCreated(new Timestamp(new Date().getTime()));
            comment.setDislikeCount(0);
            comment.setLikeCount(0);
            comment.setParentCommentId(commentRequest.getParentCommentId());
            comment.setCommentLevel(commentRequest.getCommentLevel());
            commentRepository.save(comment);
            post.setCommentCount(post.getCommentCount() + 1);
            return new BaseResponse(200, "Commented successfully");
        } catch (Exception e) {
            return new BaseResponse(500, "Internal server error");
        }
    }

    @Override
    public PostReactionResponse getReaction(int id) {
        PostReactionResponse postReactionResponse = new PostReactionResponse();
        try {
            Comment comment = commentRepository.findById(id).get();
            postReactionResponse.setLikeCount(comment.getLikeCount());
            postReactionResponse.setDislikeCount(comment.getDislikeCount());
            postReactionResponse.setStatus(200);
            postReactionResponse.setMessage("Success");
            return postReactionResponse;
        } catch (Exception e) {
            postReactionResponse.setStatus(404);
            postReactionResponse.setMessage("Post Not Found");
            return postReactionResponse;
        }
    }

    @Override
    public CommentReactionResponse checkReaction(int id, String accessToken) {
        CommentReactionResponse commentReactionResponse = new CommentReactionResponse();
        if (accessToken == null){
            commentReactionResponse.setStatus(401);
            commentReactionResponse.setMessage("Unauthorized");
            return commentReactionResponse;
        }
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            List<Reaction> reactions = reactionRepository.findAllByPostPostIdAndUser(id, user);
            List<Integer> commentIds = new ArrayList<>();
            List<String> reactTypes = new ArrayList<>();
            for (Reaction reaction : reactions) {
                if(reaction.getComment() != null) {
                    commentIds.add(reaction.getComment().getCommentId());
                    reactTypes.add(reaction.getReactType().toString());
                }
            }
            commentReactionResponse.setStatus(200);
            commentReactionResponse.setMessage("Success");
            commentReactionResponse.setCommentIds(commentIds);
            commentReactionResponse.setReactTypes(reactTypes);
            return commentReactionResponse;
        } catch (Exception e) {
            commentReactionResponse.setStatus(404);
            commentReactionResponse.setMessage("Post Not Found");
            return commentReactionResponse;
        }
    }

    @Override
    public BaseResponse reaction(int id, String accessToken, CommentReactionRequest commentReactionRequest) {
        if (accessToken == null) {
            return new BaseResponse(401, "Unauthorized");
        }
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            Comment comment = commentRepository.findById(id).get();
            Reaction reaction = reactionRepository.findByCommentAndUser(comment, user);
            if (reaction != null) {
                if (reaction.getReactType().toString().equals(commentReactionRequest.getReactType())) {
                    if (commentReactionRequest.getReactType().equals("LIKE")) {
                        comment.setLikeCount(comment.getLikeCount() - 1);
                    } else {
                        comment.setDislikeCount(comment.getDislikeCount() - 1);
                    }
                    reactionRepository.delete(reaction);
                    commentRepository.save(comment);
                    return new BaseResponse(200, "Reaction Removed");
                } else {
                    if (commentReactionRequest.getReactType().equals("LIKE")) {
                        comment.setLikeCount(comment.getLikeCount() + 1);
                        comment.setDislikeCount(comment.getDislikeCount() - 1);
                    } else {
                        comment.setDislikeCount(comment.getDislikeCount() + 1);
                        comment.setLikeCount(comment.getLikeCount() - 1);
                    }
                    reaction.setReactType(ReactionType.valueOf(commentReactionRequest.getReactType()));
                    reactionRepository.save(reaction);
                    commentRepository.save(comment);
                }
            } else {
                reaction = new Reaction();
                reaction.setComment(comment);
                reaction.setUser(user);
                reaction.setPost(postRepository.findByPostId(commentReactionRequest.getPostId()));
                reaction.setReactType(ReactionType.valueOf(commentReactionRequest.getReactType()));
                if (commentReactionRequest.getReactType().equals("LIKE")){
                    comment.setLikeCount(comment.getLikeCount() + 1);
                }
                else {
                    comment.setDislikeCount(comment.getDislikeCount() + 1);
                }
                reactionRepository.save(reaction);
            }
        } catch (Exception e) {
            return new BaseResponse(404, e.getMessage());
        }
        return new BaseResponse(200, "Reaction Added");
    }
}
