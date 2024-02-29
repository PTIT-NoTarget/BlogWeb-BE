package com.blogwebapi.service.impl;

import com.blogwebapi.dto.dto.PostDto;
import com.blogwebapi.dto.dto.TagDto;
import com.blogwebapi.dto.dto.UserDto;
import com.blogwebapi.dto.request.PostReactionRequest;
import com.blogwebapi.dto.request.PostRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.ListpostResponse;
import com.blogwebapi.dto.response.PostReactionResponse;
import com.blogwebapi.entity.*;
import com.blogwebapi.entity.constants.PostStatus;
import com.blogwebapi.entity.constants.ReactionType;
import com.blogwebapi.entity.constants.Role;
import com.blogwebapi.repository.IPostRepository;
import com.blogwebapi.repository.IReactionRepository;
import com.blogwebapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IJWTService jwtService;

    @Autowired
    private ICloudinaryService cloudinaryService;

    @Autowired
    private ITagService tagService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IReactionRepository reactionRepository;

    private UserDto getUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private List<TagDto> getTagDtos(List<Tag> tags) {
        return tags.stream().map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());
    }

    private List<PostDto> getPostDtos(List<Post> posts) {
        return posts.stream().map(post -> {
            PostDto postDto = modelMapper.map(post, PostDto.class);
            postDto.setUser(getUserDto(post.getUser()));
            postDto.setTags(getTagDtos(post.getTags()));
            return postDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> viewAllPosts() {
        List<Post> posts = postRepository.findAll();
        return getPostDtos(posts);
    }

    @Override
    public PostDto viewPost(int postId) {
        Post post = postRepository.findById(postId).get();
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setUser(getUserDto(post.getUser()));
        postDto.setTags(getTagDtos(post.getTags()));
        return postDto;
    }

    @Override
    public BaseResponse uploadPost(PostRequest postRequest, String accessToken) {
        if (accessToken == null) return new BaseResponse(401, "Unauthorized");
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        Post post = new Post();
        if (postRequest.getThumbnail().isEmpty() || postRequest.getThumbnail() == null || postRequest.getContent().isEmpty() || postRequest.getTitle().isEmpty()) {
            post.setStatus(PostStatus.DRAFT);
        } else {
            post.setThumbnail(cloudinaryService.uploadImage(postRequest.getThumbnail(), "thumbnail"));
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            post.setStatus(PostStatus.PUBLISHED);
        }
        post.setUser(user);
        post.setDateCreated(new Timestamp(new Date().getTime()));
        post.setDislikeCount(0);
        post.setLikeCount(0);
        post.setViewCount(0);
        post.setCommentCount(0);
        List<Tag> blogTags = new ArrayList<>();
        for (String tagName : postRequest.getTags()) {
            Tag tag = new Tag();
            if (tagService.findTagByName(tagName).getStatus() == 404) {
                tag.setName(tagName);
                tag.setCount(1);
                tagService.addTag(tag);
            } else {
                tag = tagService.getTagByName(tagName);
                tag.setCount(tag.getCount() + 1);
            }
            blogTags.add(tag);
        }
        post.setTags(blogTags);
        postRepository.save(post);
        return new BaseResponse(200, "Post Uploaded Successfully");
    }

    @Override
    public BaseResponse deletePost(int postId, String accessToken) {
        try {
            User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
            if (user.getRole() == Role.USER && user.getUserId() != postRepository.findById(postId).get().getUser().getUserId()) {
                return new BaseResponse(401, "You are not authorized to delete this post");
            }
            postRepository.deleteById(postId);
        } catch (Exception e) {
            return new BaseResponse(404, "Post Not Found");
        }
        return new BaseResponse(200, "Post Deleted Successfully");
    }

    @Override
    public BaseResponse updatePost(int postId, PostRequest postRequest, String accessToken) {
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            Post post = postRepository.findById(postId).get();
            if (user.getUserId() != post.getUser().getUserId() && user.getRole() == Role.USER) {
                return new BaseResponse(401, "You are not authorized to update this post");
            }
            if (postRequest.getThumbnail() != null) {
                post.setThumbnail(cloudinaryService.uploadImage(postRequest.getThumbnail(), "thumbnail"));
            }
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            post.setUser(user);
            postRepository.save(post);
        } catch (Exception e) {
            return new BaseResponse(404, "Post Not Found");
        }
        return new BaseResponse(200, "Post Updated Successfully");
    }

    @Override
    public PostReactionResponse getReaction(int id) {
        PostReactionResponse postReactionResponse = new PostReactionResponse();
        try {
            Post post = postRepository.findById(id).get();
            postReactionResponse.setLikeCount(post.getLikeCount());
            postReactionResponse.setDislikeCount(post.getDislikeCount());
            postReactionResponse.setCommentCount(post.getCommentCount());
            postReactionResponse.setViewCount(post.getViewCount());
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
    public BaseResponse checkReaction(int id, String accessToken) {
        if (accessToken == null) return new BaseResponse(401, "Unauthorized");
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            List<Reaction> reactions = reactionRepository.findAllByPostPostId(id);
            for (Reaction reaction : reactions) {
                if (reaction.getUser().getUserId() == user.getUserId() && reaction.getComment() == null) {
                    return new BaseResponse(200, reaction.getReactType().toString());
                }
            }
            return new BaseResponse(200, "NULL");
        } catch (Exception e) {
            return new BaseResponse(404, "Post Not Found");
        }
    }

    @Override
    public BaseResponse countView(int id) {
        try {
            Post post = postRepository.findById(id).get();
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        } catch (Exception e) {
            return new BaseResponse(404, "Post Not Found");
        }
        return new BaseResponse(200, "View Count Updated");
    }

    @Override
    public List<PostDto> get5MostViewPosts() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Post> posts = postRepository.findTop5ByOrderByViewCountDesc(pageRequest);
        return getPostDtos(posts);
    }

    @Override
    public ListpostResponse viewPostsByPage(int page, int pageSize) {
        ListpostResponse listpostResponse = new ListpostResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Post> posts = postRepository.findAll(pageRequest).getContent();
        listpostResponse.setPostList(getPostDtos(posts));
        int totalPost = postRepository.countAllByStatus(PostStatus.PUBLISHED);
        listpostResponse.setTotalPost(totalPost);
        listpostResponse.setTotalPage((int) Math.ceil((double) totalPost / pageSize));
        return listpostResponse;
    }

    @Override
    public BaseResponse reaction(int id, String accessToken, PostReactionRequest postReactionRequest) {
        if (accessToken == null) {
            return new BaseResponse(401, "Unauthorized");
        }
        User user = userService.getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            Post post = postRepository.findById(id).get();
            Reaction reaction = reactionRepository.findByPostAndUserAndComment(post, user, null);
            if (reaction != null) {
                if (reaction.getReactType().toString().equals(postReactionRequest.getReactType())) {
                    if (postReactionRequest.getReactType().equals("LIKE")) {
                        post.setLikeCount(post.getLikeCount() - 1);
                    } else {
                        post.setDislikeCount(post.getDislikeCount() - 1);
                    }
                    reactionRepository.delete(reaction);
                    postRepository.save(post);
                    return new BaseResponse(200, "Reaction Removed");
                } else {
                    if (postReactionRequest.getReactType().equals("LIKE")) {
                        post.setLikeCount(post.getLikeCount() + 1);
                        post.setDislikeCount(post.getDislikeCount() - 1);
                    } else {
                        post.setDislikeCount(post.getDislikeCount() + 1);
                        post.setLikeCount(post.getLikeCount() - 1);
                    }
                    reaction.setReactType(ReactionType.valueOf(postReactionRequest.getReactType()));
                    reactionRepository.save(reaction);
                    postRepository.save(post);
                }
            } else {
                reaction = new Reaction();
                reaction.setPost(post);
                reaction.setUser(user);
                reaction.setReactType(ReactionType.valueOf(postReactionRequest.getReactType()));
                if (postReactionRequest.getReactType().equals("LIKE")){
                    post.setLikeCount(post.getLikeCount() + 1);
                }
                else {
                    post.setDislikeCount(post.getDislikeCount() + 1);
                }
                reactionRepository.save(reaction);
            }
        } catch (Exception e) {
            return new BaseResponse(404, e.getMessage());
        }
        return new BaseResponse(200, "Reaction Added");
    }


}
