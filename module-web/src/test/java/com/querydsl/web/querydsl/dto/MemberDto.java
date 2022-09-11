package com.querydsl.web.querydsl.dto;

import com.querydsl.core.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private int age;
    private TeamDto team;

    public MemberDto(Member member, TeamDto team) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.age = member.getAge();
        this.team = team;
    }
}
