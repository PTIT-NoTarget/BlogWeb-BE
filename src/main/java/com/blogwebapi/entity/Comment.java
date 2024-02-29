package com.blogwebapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Table(name = "Comments")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int commentId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "postId", nullable = false)
        private Post post;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId", nullable = false)
        private User user;

        @Column(name = "content", nullable = false)
        private String content;

        @Column(name = "date_created", nullable = false)
        private Timestamp dateCreated;

        @Column(name = "parent_comment_id", nullable = false)
        private int parentCommentId;

        @Column(name = "comment_level", nullable = false)
        private int commentLevel;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
        private List<Reaction> reactions;

        @Column(name = "like_count", nullable = false)
        private int likeCount;

        @Column(name = "dislike_count", nullable = false)
        private int dislikeCount;
}