package com.blogwebapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse extends BaseResponse{
    private int userId;
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatar;
    private String role;
}
