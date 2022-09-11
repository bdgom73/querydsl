package com.querydsl.web.repository.team;

import com.querydsl.core.entity.QTeam;
import com.querydsl.core.entity.Team;
import com.querydsl.core.repository.team.TeamRepositoryCustom;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> findAllByNotDelete() {
        return queryFactory.selectFrom(team)
                .where(deleteNot())
                .fetch();
    }

    private BooleanExpression deleteNot() {
        return team.deleteYn.eq("N");
    }
}
