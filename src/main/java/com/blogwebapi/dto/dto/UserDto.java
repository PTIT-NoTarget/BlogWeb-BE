package com.blogwebapi.dto.dto;

import com.blogwebapi.entity.constants.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private int userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatar;
    private String username;
    private Role role;
}
