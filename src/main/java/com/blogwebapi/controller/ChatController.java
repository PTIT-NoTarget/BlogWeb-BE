package com.blogwebapi.controller;

import com.blogwebapi.dto.dto.ChatMessageDto;
import com.blogwebapi.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

//    @MessageMapping("/notification")
//    public ChatMessageDto receiveNotification(@Payload ChatMessageDto chatMessage) {
//        simpMessagingTemplate.convertAndSendToUser(
//                chatMessage.getReceiver().getUsername(), "/private", chatMessage);
//        return chatMessage;
//    }

    @MessageMapping("/message")
    @SendTo("/topic/private")
    public ChatMessageDto receiveMessage(@Payload ChatMessageDto chatMessage) {
        simpMessagingTemplate.convertAndSend("/user/" + chatMessage.getReceiverId(), chatMessage);
        return chatMessage;
    }
}
