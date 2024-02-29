package com.blogwebapi.repository;

import com.blogwebapi.entity.Resources;
import com.blogwebapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IResourcesRepository extends JpaRepository<Resources, Integer> {
    List<Resources> findAllByUser(User user);
    Resources findResourcesByUser(User user);
}
