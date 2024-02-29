package com.blogwebapi.dto.request;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UploadImageRequest {
    private MultipartFile image;
    private String name;
}
