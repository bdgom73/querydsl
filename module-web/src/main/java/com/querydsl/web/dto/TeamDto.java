package com.querydsl.web.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDto {
    private Long id;
    private String name;

    public TeamDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TeamDto(String name) {
        this.name = name;
    }
}
