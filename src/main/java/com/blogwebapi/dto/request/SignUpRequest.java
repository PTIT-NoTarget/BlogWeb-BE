package com.blogwebapi.dto.request;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpRequest {
    private MultipartFile avatar;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

}
