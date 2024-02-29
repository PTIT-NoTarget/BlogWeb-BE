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
public class BlogWebApiApplication implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication.run(BlogWebApiApplication.class, args);
    }

    @Autowired
    private IUserRepository userRepository;

    public void run(String... args) {
        if(userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setFullName("Admin");
            user.setEmail("ngodanghan662003@gmail.com");
            user.setPhoneNumber("0963439807");
            user.setUsername("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}
