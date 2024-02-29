package com.blogwebapi.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostReactionRequest {
    private String reactType;
}
