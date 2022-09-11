package com.querydsl.web.service;

import com.querydsl.core.entity.Member;
import com.querydsl.core.entity.QMember;
import com.querydsl.core.entity.QTeam;
import com.querydsl.core.entity.Team;
import com.querydsl.core.repository.member.MemberRepository;
import com.querydsl.core.repository.team.TeamRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    JPAQueryFactory queryFactory;

    @PersistenceContext
    EntityManager em;

    @Test
    void  default_batch_fetch_size() {

        Team teamA = teamRepository.save(new Team("teamA"));
        Team teamB = teamRepository.save(new Team("teamB"));

        memberRepository.save(new Member("userA", 10, teamA));
        memberRepository.save(new Member("userB", 20, teamB));
        memberRepository.save(new Member("userC", 30, teamA));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        /**
         * 첫번째 쿼리
         *   select
         *         member0_.member_id as member_i1_0_,
         *         member0_.age as age2_0_,
         *         member0_.team_id as team_id4_0_,
         *         member0_.username as username3_0_
         *     from
         *         member member0_
         * */

        /**
         * 두번째 쿼리
         * 이미 teamA 는 영속 상태이므로 db 조회 없이 영속성 컨텍스트에서 꺼내 사용한다.
         *  select
         *         team0_.team_id as team_id1_1_0_,
         *         team0_.delete_yn as delete_y2_1_0_,
         *         team0_.name as name3_1_0_
         *     from
         *         team team0_
         *     where
         *         team0_.team_id in (
         *             ?, ?
         *         )
         * */
        // 배치 패치 사이즈 설정으로 인해 n+1 문제 해결
        for (Member member : members) {
             member.getTeam().getName();
        }
    }

    @Test
    void test2() {
        Team teamA = teamRepository.save(new Team("teamA"));
        Team teamB = teamRepository.save(new Team("teamB"));

        memberRepository.save(new Member("userA", 10, teamA));
        memberRepository.save(new Member("userB", 20, teamB));
        memberRepository.save(new Member("userC", 30, teamA));

        em.flush();
        em.clear();

        QMember member = QMember.member;
        QTeam team = QTeam.team;
        Member n = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.deleteYn.eq("N"))
                .fetchFirst();

        System.out.println("memberDto = " + n);
    }

}