package com.blogwebapi.controller;

import com.blogwebapi.dto.dto.ChatBoxDto;
import com.blogwebapi.dto.dto.ResourceDto;
import com.blogwebapi.dto.request.*;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.CommentReactionResponse;
import com.blogwebapi.dto.response.ProfileResponse;
import com.blogwebapi.service.ICommentService;
import com.blogwebapi.service.IPostService;
import com.blogwebapi.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private final IPostService postService;

    @Autowired
    private final IUserService userService;

    @Autowired
    private final ICommentService commentService;


    @PostMapping("/upload-post")
    public ResponseEntity<BaseResponse> upPost(@ModelAttribute PostRequest postRequest, @RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(postService.uploadPost(postRequest, accessToken), HttpStatus.OK);
    }

    @PostMapping("/post/{id}")
    public ResponseEntity<BaseResponse> editPost(@PathVariable int id, @ModelAttribute PostRequest postRequest, @RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(postService.updatePost(id, postRequest, accessToken), HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable int id, @RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(postService.deletePost(id, accessToken), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> profile(@RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(userService.getProfileByToken(accessToken), HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<BaseResponse> uploadImage(@ModelAttribute UploadImageRequest image, @RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(userService.uploadImage(image, accessToken), HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<List<ResourceDto>> getAllImage(@RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(userService.getAllImage(accessToken), HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<BaseResponse> deleteImage(@RequestHeader("accessToken") String accessToken, @RequestBody DeleteRequest deleteRequest) {
        return new ResponseEntity<>(userService.deleteImage(accessToken, deleteRequest), HttpStatus.OK);
    }

    @PostMapping("/post-reaction/{id}")
    public ResponseEntity<BaseResponse> getPostReaction(@PathVariable int id, @RequestHeader("accessToken") String accessToken, @RequestBody PostReactionRequest postReactionRequest) {
        return new ResponseEntity<>(postService.reaction(id, accessToken, postReactionRequest), HttpStatus.OK);
    }

    @GetMapping("/check-post-reaction/{id}")
    public ResponseEntity<BaseResponse> checkPostReaction(@PathVariable int id, @RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(postService.checkReaction(id, accessToken), HttpStatus.OK);
    }

    @PostMapping("/comment-reaction/{id}")
    public ResponseEntity<BaseResponse> getCommentReaction(@PathVariable int id, @RequestHeader("accessToken") String accessToken, @RequestBody CommentReactionRequest commentReactionRequest) {
        return new ResponseEntity<>(commentService.reaction(id, accessToken, commentReactionRequest), HttpStatus.OK);
    }

    @GetMapping("/check-comment-reaction/{id}")
    public ResponseEntity<CommentReactionResponse> checkCommentReaction(@PathVariable int id, @RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(commentService.checkReaction(id, accessToken), HttpStatus.OK);
    }

    @PostMapping("/comment/{postId}")
    public ResponseEntity<BaseResponse> commentPost(@PathVariable int postId, @RequestHeader("accessToken") String accessToken, @RequestBody CommentRequest commentRequest) {
        System.out.println(accessToken);
        return new ResponseEntity<>(commentService.commentPost(postId, accessToken, commentRequest), HttpStatus.OK);
    }

    @GetMapping("/chatbox")
    public ResponseEntity<List<ChatBoxDto>> getChatBox(@RequestHeader("accessToken") String accessToken) {
        return new ResponseEntity<>(userService.getChatBox(accessToken), HttpStatus.OK);
    }

    @PostMapping("/chatbox")
    public ResponseEntity<BaseResponse> createChatBox(@RequestHeader("accessToken") String accessToken, @RequestBody ChatBoxRequest chatBoxRequest) {
        return new ResponseEntity<>(userService.createChatBox(accessToken, chatBoxRequest), HttpStatus.OK);
    }

    @PostMapping("/message")
    public ResponseEntity<BaseResponse> sendMessage(@RequestHeader("accessToken") String accessToken, @RequestBody MessageRequest messageRequest) {
        return new ResponseEntity<>(userService.sendMessage(accessToken, messageRequest), HttpStatus.OK);
    }
}

