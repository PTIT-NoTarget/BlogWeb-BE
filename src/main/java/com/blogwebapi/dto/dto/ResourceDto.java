package com.blogwebapi.dto.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResourceDto {
    private int id;
    private String name;
    private String link;
    private String type;
    private UserDto user;
}
