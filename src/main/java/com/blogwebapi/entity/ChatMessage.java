package com.blogwebapi.entity;

import com.blogwebapi.entity.constants.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "chatmessages")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "chatbox_id", nullable = false)
    private ChatBox chatBox;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "message_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
}
