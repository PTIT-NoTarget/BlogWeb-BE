package com.blogwebapi.service.impl;

import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.JwtAuthenticationResponse;
import com.blogwebapi.dto.request.RefreshTokenRequest;
import com.blogwebapi.dto.request.SignInRequest;
import com.blogwebapi.dto.request.SignUpRequest;
import com.blogwebapi.entity.constants.Role;
import com.blogwebapi.entity.User;
import com.blogwebapi.repository.IUserRepository;
import com.blogwebapi.service.IAuthenticationService;
import com.blogwebapi.service.ICloudinaryService;
import com.blogwebapi.service.IJWTService;
import com.blogwebapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IJWTService jwtService;

    @Autowired
    private ICloudinaryService cloudinaryService;

    public BaseResponse signUp(SignUpRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new BaseResponse(400, "Username already exists");
        }
        User user = new User();
        user.setAvatar(cloudinaryService.uploadImage(signUpRequest.getAvatar(), "avatar"));
        user.setFullName(signUpRequest.getFullName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);
        userService.createUser(user);
        return new BaseResponse(200, "User Created Successfully");
    }

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setAccessToken(accessToken);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setRole(user.getRole().name());
        jwtAuthenticationResponse.setStatus(200);
        jwtAuthenticationResponse.setMessage("Login Successful");
        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        var Id = jwtService.extractUserId(refreshToken);
        User user = userRepository.findById(Integer.parseInt(Id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        if (jwtService.isTokenValid(refreshToken, user)) {
            var jwt = jwtService.generateToken(user);
            jwtAuthenticationResponse.setAccessToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);
            jwtAuthenticationResponse.setStatus(200);
            jwtAuthenticationResponse.setMessage("Token Refreshed Successfully");
            return jwtAuthenticationResponse;
        }
        jwtAuthenticationResponse.setStatus(401);
        jwtAuthenticationResponse.setMessage("Invalid Refresh Token");
        return jwtAuthenticationResponse;
    }
}
