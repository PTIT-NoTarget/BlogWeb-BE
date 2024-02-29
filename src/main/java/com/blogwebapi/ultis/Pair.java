package com.blogwebapi.ultis;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Pair <T, U> {
    private T first;
    private U second;
}
