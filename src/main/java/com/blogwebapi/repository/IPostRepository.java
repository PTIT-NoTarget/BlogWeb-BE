package com.blogwebapi.repository;

import com.blogwebapi.entity.Post;
import com.blogwebapi.entity.constants.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p ORDER BY p.viewCount DESC")
    List<Post> findTop5ByOrderByViewCountDesc(Pageable pageable);

    int countAllByStatus(PostStatus status);

    Post findByPostId(int postId);
}
