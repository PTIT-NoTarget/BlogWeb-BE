package com.blogwebapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    String uploadImage(MultipartFile multipartFile, String folder);
}
