package com.blogwebapi.service;

import com.blogwebapi.dto.dto.ChatBoxDto;
import com.blogwebapi.dto.dto.ResourceDto;
import com.blogwebapi.dto.dto.UserDto;
import com.blogwebapi.dto.request.ChatBoxRequest;
import com.blogwebapi.dto.request.DeleteRequest;
import com.blogwebapi.dto.request.MessageRequest;
import com.blogwebapi.dto.request.UploadImageRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.ProfileResponse;
import com.blogwebapi.entity.Resources;
import com.blogwebapi.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService {
    List<User> getAllUser();
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(Integer id);
    User getUserById(Integer id);
    ProfileResponse getProfileByToken(String token);
    UserDetailsService userDetailsService();
    boolean existsByUsername(String username);
    BaseResponse uploadImage(UploadImageRequest image, String accessToken);
    List<ResourceDto> getAllImage(String accessToken);
    BaseResponse deleteImage(String accessToken, DeleteRequest deleteRequest);
    List<ChatBoxDto> getChatBox(String accessToken);

    BaseResponse createChatBox(String accessToken, ChatBoxRequest chatBoxRequest);

    UserDto getProfileByUsername(String username);

    BaseResponse sendMessage(String accessToken, MessageRequest messageRequest);
}
