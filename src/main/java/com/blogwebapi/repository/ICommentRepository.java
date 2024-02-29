package com.blogwebapi.repository;

import com.blogwebapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPostPostId(int postId);
}
