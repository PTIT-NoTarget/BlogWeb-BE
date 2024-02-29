package com.blogwebapi.dto.request;

import com.blogwebapi.entity.Tag;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostRequest {
    private MultipartFile thumbnail;
    private String title;
    private String content;
    private List<String> tags;
}
