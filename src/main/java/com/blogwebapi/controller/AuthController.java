package com.blogwebapi.controller;

import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.JwtAuthenticationResponse;
import com.blogwebapi.dto.request.RefreshTokenRequest;
import com.blogwebapi.dto.request.SignInRequest;
import com.blogwebapi.dto.request.SignUpRequest;
import com.blogwebapi.entity.User;
import com.blogwebapi.service.IAuthenticationService;
import com.blogwebapi.service.ICloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final IAuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signUp(@ModelAttribute SignUpRequest signUpRequest) {
        return new ResponseEntity<>(authenticationService.signUp(signUpRequest), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(authenticationService.signIn(signInRequest), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authenticationService.refreshToken(refreshTokenRequest), HttpStatus.OK);
    }
}

