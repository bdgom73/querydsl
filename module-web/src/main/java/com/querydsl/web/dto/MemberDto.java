package com.querydsl.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private int age;

    public MemberDto(Long id, String username, int age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }
}
