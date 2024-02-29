package com.blogwebapi.dto.dto;

import com.blogwebapi.entity.ChatMessage;
import com.blogwebapi.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatBoxDto {
    private int id;
    private UserDto user1;
    private UserDto user2;
    private List<ChatMessageDto> chatMessages;
    private Timestamp dateCreated;
    private Timestamp lastMessageTime;
    private String lastMessage;
}
