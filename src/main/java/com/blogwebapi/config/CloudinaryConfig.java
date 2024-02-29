package com.blogwebapi.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary("cloudinary://791132358192565:GPBVYs9ineLLpSaKmBkQyP-uxYs@blogweb");
    }
}
