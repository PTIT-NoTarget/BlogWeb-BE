package com.blogwebapi.repository;

import com.blogwebapi.entity.ChatBox;
import com.blogwebapi.entity.ChatMessage;
import com.blogwebapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChatBoxRepository extends JpaRepository<ChatBox, Integer> {
    @Query("SELECT c FROM ChatBox c WHERE c.user1 = ?1 OR c.user2 = ?1")
    List<ChatBox> findAllByUser(User user);

    @Query("SELECT c FROM ChatBox c WHERE c.user1 = ?1 OR c.user2 = ?1 ORDER BY c.lastMessageTime DESC")
    List<ChatBox> findAllByUserOrderByLastMessageTime(User user);

    @Query("SELECT c FROM ChatBox c WHERE (c.user1 = ?1 AND c.user2 = ?2) OR (c.user1 = ?2 AND c.user2 = ?1)")
    ChatBox findByUser1AndUser2(User user1, User user2);
}
