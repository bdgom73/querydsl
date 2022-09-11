package com.querydsl.web.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.web.repository.dto.MemberTeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.querydsl.core.entity.QMember.member;
import static com.querydsl.core.entity.QTeam.team;

/**
 * Member dto 또는 복잡한 쿼리작성시 사용하는 repository
 * */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberTeamDto findByIdFetchTeam(Long id) {

        MemberTeamDto memberTeamDto = queryFactory
                .select(Projections.fields(MemberTeamDto.class,
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .on(deleteNot())
                .where(member.id.eq(id))
                .fetchOne();
        return memberTeamDto;
    }
    private BooleanExpression deleteNot() {
        return team.deleteYn.eq("N");
    }
}
