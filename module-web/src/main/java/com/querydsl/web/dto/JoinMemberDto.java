package com.querydsl.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinMemberDto {

    private String username;
    private int age;

    private Long teamId;

    public JoinMemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public JoinMemberDto(String username, int age, Long teamId) {
        this.username = username;
        this.age = age;
        this.teamId = teamId;
    }
}
