package com.querydsl.core.repository.member;

import com.querydsl.core.entity.Member;
import java.util.Optional;
/**
 * Member entity 를 querydsl 을 통해 조회하는 경우 사용하는 인터페이스
 * */
public interface MemberRepositoryCustom {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByIdFetchTeam(Long id);
}
