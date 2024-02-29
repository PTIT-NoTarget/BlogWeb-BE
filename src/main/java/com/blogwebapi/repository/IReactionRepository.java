package com.blogwebapi.repository;

import com.blogwebapi.entity.Comment;
import com.blogwebapi.entity.Post;
import com.blogwebapi.entity.Reaction;
import com.blogwebapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReactionRepository extends JpaRepository<Reaction, Integer> {
    List <Reaction> findAllByPostPostId(int postId);
    Reaction findByPostAndUserAndComment(Post post, User user, Comment comment);
    List <Reaction> findAllByCommentCommentId(int commentId);
    Reaction findByCommentAndUser(Comment comment, User user);
    List <Reaction> findAllByPostPostIdAndUser(int postId, User user);
}
