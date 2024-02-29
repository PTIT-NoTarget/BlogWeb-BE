package com.blogwebapi.dto.request;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRequest {
    private String content;
    private int receiverId;
    private int chatBoxId;
    private Timestamp dateCreated;
}
