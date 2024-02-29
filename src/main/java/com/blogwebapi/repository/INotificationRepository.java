package com.blogwebapi.repository;

import com.blogwebapi.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationRepository extends JpaRepository<Notifications, Integer> {
}
