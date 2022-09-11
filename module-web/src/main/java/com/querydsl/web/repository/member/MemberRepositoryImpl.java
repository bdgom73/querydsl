package com.querydsl.web.repository.member;

import com.querydsl.core.entity.Member;
import com.querydsl.core.entity.QTeam;
import com.querydsl.core.repository.member.MemberRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.web.repository.dto.MemberTeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.querydsl.core.entity.QMember.member;
import static com.querydsl.core.entity.QTeam.*;

/**
 * Member Entity 를 반환받는 쿼리 구현부
 * */
@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public Optional<Member> findByUsername(String username) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(member)
                        .where(member.username.eq(username))
                        .fetchOne()
        );
    }

    @Override
    public Optional<Member> findByIdFetchTeam(Long id) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(member)
                        .join(member.team, team).fetchJoin()
                        .where(member.id.eq(id))
                        .fetchOne()
        );
    }


}
