package com.blogwebapi.service;

import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.JwtAuthenticationResponse;
import com.blogwebapi.dto.request.RefreshTokenRequest;
import com.blogwebapi.dto.request.SignInRequest;
import com.blogwebapi.dto.request.SignUpRequest;
import com.blogwebapi.entity.User;

public interface IAuthenticationService {
    BaseResponse signUp(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signIn(SignInRequest signInRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
