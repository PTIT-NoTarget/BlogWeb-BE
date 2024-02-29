package com.blogwebapi.dto.dto;

import com.blogwebapi.entity.User;
import com.blogwebapi.entity.constants.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessageDto {
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp dateCreated;
}
