package com.blogwebapi;

import com.blogwebapi.entity.constants.Role;
import com.blogwebapi.entity.User;
import com.blogwebapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BlogWebApiApplication{
    public static void main(String[] args) {
        SpringApplication.run(BlogWebApiApplication.class, args);
    }
}
