package com.blogwebapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Table(name = "Notifications")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "receiverId", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false)
    private User sender;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "commentId", nullable = true)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "chatBoxId", nullable = true)
    private ChatBox chatBox;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;
}
