package com.blogwebapi.dto.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TagDto {
    private int tagId;
    private String name;
    private int count;
}
