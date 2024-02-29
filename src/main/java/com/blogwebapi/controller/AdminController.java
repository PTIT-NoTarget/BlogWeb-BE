package com.blogwebapi.controller;

import com.blogwebapi.dto.request.PostRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.entity.Post;
import com.blogwebapi.service.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

}
