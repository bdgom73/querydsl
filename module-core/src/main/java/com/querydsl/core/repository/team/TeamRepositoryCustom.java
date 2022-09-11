package com.querydsl.core.repository.team;

import com.querydsl.core.entity.Team;

import java.util.List;
import java.util.Optional;

/**
 * Team entity 를 querydsl 을 통해 조회하는 경우 사용하는 인터페이스
 * */
public interface TeamRepositoryCustom {

    List<Team> findAll();

    Optional<Team> findQueryById(Long id);
}
