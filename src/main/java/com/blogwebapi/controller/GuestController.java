package com.blogwebapi.controller;

import com.blogwebapi.dto.dto.CommentDto;
import com.blogwebapi.dto.dto.PostDto;
import com.blogwebapi.dto.dto.TagDto;
import com.blogwebapi.dto.dto.UserDto;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.ListpostResponse;
import com.blogwebapi.dto.response.PostReactionResponse;
import com.blogwebapi.service.ICommentService;
import com.blogwebapi.service.IPostService;
import com.blogwebapi.service.ITagService;
import com.blogwebapi.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guest")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GuestController {
    @Autowired
    private final ITagService tagService;

    @Autowired
    private final IPostService postService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ICommentService commentService;

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> viewPost(@PathVariable int id) {
        return new ResponseEntity<>(postService.viewPost(id), HttpStatus.OK);
    }

    @GetMapping("/view-all-posts")
    public ResponseEntity<List<PostDto>> viewPost() {
        return new ResponseEntity<>(postService.viewAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> getAllTags() {
        return new ResponseEntity<>(tagService.viewAllTags(), HttpStatus.OK);
    }

    @GetMapping("/post-reaction/{id}")
    public ResponseEntity<PostReactionResponse> getReaction(@PathVariable int id) {
        return new ResponseEntity<>(postService.getReaction(id), HttpStatus.OK);
    }

    @GetMapping("/comment-reaction/{id}")
    public ResponseEntity<PostReactionResponse> getReactionComment(@PathVariable int id) {
        return new ResponseEntity<>(commentService.getReaction(id), HttpStatus.OK);
    }

    @PostMapping("/view/{id}")
    public ResponseEntity<BaseResponse> countView(@PathVariable int id) {
        return new ResponseEntity<>(postService.countView(id), HttpStatus.OK);
    }

    @GetMapping("/view-posts/{page}")
    public ResponseEntity<ListpostResponse> viewPost(@PathVariable int page,
                                                     @RequestParam int pageSize) {
        return new ResponseEntity<>(postService.viewPostsByPage(page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/get-5-most-view-posts")
    public ResponseEntity<List<PostDto>> get5MostViewPosts() {
        return new ResponseEntity<>(postService.get5MostViewPosts(), HttpStatus.OK);
    }

    @GetMapping("/comment/{postId}")
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable int postId) {
        return new ResponseEntity<>(commentService.getAllComments(postId), HttpStatus.OK);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserDto> getProfile(@PathVariable String username) {
        return new ResponseEntity<>(userService.getProfileByUsername(username), HttpStatus.OK);
    }
}
