package com.blogwebapi.service.impl;

import com.blogwebapi.dto.dto.ChatBoxDto;
import com.blogwebapi.dto.dto.ChatMessageDto;
import com.blogwebapi.dto.dto.ResourceDto;
import com.blogwebapi.dto.dto.UserDto;
import com.blogwebapi.dto.request.ChatBoxRequest;
import com.blogwebapi.dto.request.DeleteRequest;
import com.blogwebapi.dto.request.MessageRequest;
import com.blogwebapi.dto.request.UploadImageRequest;
import com.blogwebapi.dto.response.BaseResponse;
import com.blogwebapi.dto.response.ProfileResponse;
import com.blogwebapi.entity.*;
import com.blogwebapi.entity.constants.MessageType;
import com.blogwebapi.repository.IChatBoxRepository;
import com.blogwebapi.repository.IChatMessageRepository;
import com.blogwebapi.repository.IResourcesRepository;
import com.blogwebapi.repository.IUserRepository;
import com.blogwebapi.service.ICloudinaryService;
import com.blogwebapi.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ICloudinaryService cloudinaryService;

    @Autowired
    private IResourcesRepository resourcesRepository;

    @Autowired
    private IChatBoxRepository chatBoxRepository;

    @Autowired
    private IChatMessageRepository chatMessageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<User> getAllUser() {
        return userService.findAll();
    }

    @Override
    public void createUser(User user) {
        userService.save(user);
    }

    @Override
    public void updateUser(User user) {
        userService.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userService.deleteById(id);
    }

    @Override
    public User getUserById(Integer id) {
        return userService.findById(id).get();
    }

    @Override
    public ProfileResponse getProfileByToken(String token) {
        ProfileResponse profileResponse = new ProfileResponse();
        try {
            String userId = jwtService.extractUserId(token);
            System.out.println(userId);
            User user = userService.findById(Integer.parseInt(userId)).get();
            profileResponse.setFullName(user.getFullName());
            profileResponse.setAvatar(user.getAvatar());
            profileResponse.setUsername(user.getUsername());
            profileResponse.setEmail(user.getEmail());
            profileResponse.setPhoneNumber(user.getPhoneNumber());
            profileResponse.setRole(user.getRole().toString());
            profileResponse.setUserId(user.getUserId());
            profileResponse.setStatus(200);
            profileResponse.setMessage("Success");
            return profileResponse;
        } catch (Exception e) {
            profileResponse.setStatus(500);
            profileResponse.setMessage(e.getMessage());
            return profileResponse;
        }
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userService.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
            }
        };
    }

    @Override
    public boolean existsByUsername(String username) {
        return userService.existsByUsername(username);
    }

    @Override
    public BaseResponse uploadImage(UploadImageRequest image, String accessToken) {
        if (accessToken == null) return new BaseResponse(401, "Unauthorized");
        User user = getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        Resources resources = new Resources();
        resources.setUser(user);
        resources.setLink(cloudinaryService.uploadImage(image.getImage(), "resources"));
        resources.setName(image.getName());
        resources.setType("image");
        resourcesRepository.save(resources);
        return new BaseResponse(200, "Image Uploaded Successfully");
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<ResourceDto> getAllImage(String accessToken) {
        if (accessToken == null) return null;
        User user = getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        List<Resources> resources = resourcesRepository.findAllByUser(user);
        return resources.stream().map(resource -> {
            ResourceDto resourceDto = modelMapper.map(resource, ResourceDto.class);
            resourceDto.setUser(convertToDto(resource.getUser()));
            return resourceDto;
        }).collect(Collectors.toList());
    }

    @Override
    public BaseResponse deleteImage(String accessToken, DeleteRequest deleteRequest) {
        if (accessToken == null) return new BaseResponse(401, "Unauthorized");
        User user = getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try {
            Resources resources = resourcesRepository.findById(Integer.parseInt(deleteRequest.getId())).get();
            if (user.getUserId() != resources.getUser().getUserId()) {
                return new BaseResponse(401, "You are not authorized to delete this image");
            }
            resourcesRepository.deleteById(Integer.parseInt(deleteRequest.getId()));
        } catch (Exception e) {
            return new BaseResponse(404, "Image Not Found");
        }
        return new BaseResponse(200, "Image Deleted Successfully");
    }

    @Override
    public List<ChatBoxDto> getChatBox(String accessToken) {
        if (accessToken == null) return null;
        User user = getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        List<ChatBox> chatBoxDtos = chatBoxRepository.findAllByUserOrderByLastMessageTime(user);
        return chatBoxDtos.stream().map(chatBox -> {
            ChatBoxDto chatBoxDto = modelMapper.map(chatBox, ChatBoxDto.class);
            List<ChatMessage> chatMessages = chatBox.getChatMessages();
            chatBoxDto.setUser1(convertToDto(chatBox.getUser1()));
            chatBoxDto.setUser2(convertToDto(chatBox.getUser2()));
            chatBoxDto.setChatMessages(chatMessages.stream().map(chatMessage -> modelMapper.map(chatMessage, ChatMessageDto.class)).collect(Collectors.toList()));
            return chatBoxDto;
        }).collect(Collectors.toList());

    }

    @Override
    public BaseResponse createChatBox(String accessToken, ChatBoxRequest chatBoxRequest) {
        if (accessToken == null) return new BaseResponse(401, "Unauthorized");
        User user1 = getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        User user2 = userService.findByUsername(chatBoxRequest.getUsername()).get();
        if (user1.getUserId() == user2.getUserId()) {
            return new BaseResponse(400, "You cannot create chat box with yourself");
        }
        if (chatBoxRepository.findByUser1AndUser2(user1, user2) != null) {
            return new BaseResponse(200, "Chat Box Already Exists");
        }
        ChatBox chatBox = new ChatBox();
        chatBox.setUser1(user1);
        chatBox.setUser2(user2);
        chatBox.setDateCreated(new Timestamp(new Date().getTime()));
        chatBoxRepository.save(chatBox);
        return new BaseResponse(200, "Chat Box Created Successfully");
    }

    @Override
    public UserDto getProfileByUsername(String username) {
        User user = userService.findByUsername(username).get();
        return convertToDto(user);
    }

    @Override
    public BaseResponse sendMessage(String accessToken, MessageRequest messageRequest) {
        if (accessToken == null) return new BaseResponse(401, "Unauthorized");
        User user = getUserById(Integer.parseInt(jwtService.extractUserId(accessToken)));
        try{
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(user);
            chatMessage.setReceiver(getUserById(messageRequest.getReceiverId()));
            chatMessage.setChatBox(chatBoxRepository.findById(messageRequest.getChatBoxId()).get());
            chatMessage.setContent(messageRequest.getContent());
            chatMessage.setMessageType(MessageType.CHAT);
            chatMessage.setDateCreated(messageRequest.getDateCreated());
            chatMessageRepository.save(chatMessage);
        }catch (Exception e){
            return new BaseResponse(404, e.getMessage());
        }
        return new BaseResponse(200, "Message Sent Successfully");
    }
}
