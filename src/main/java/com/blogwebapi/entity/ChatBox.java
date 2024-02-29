package com.blogwebapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Table(name = "chatbox")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @OneToMany(mappedBy = "chatBox", fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessages;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @Column(name = "last_message_time", nullable = true)
    private Time lastMessageTime;

    @Column(name = "last_message", nullable = true)
    private String lastMessage;
}
