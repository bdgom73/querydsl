package com.querydsl.web.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.entity.Member;
import com.querydsl.core.entity.QMember;
import com.querydsl.core.entity.QTeam;
import com.querydsl.core.entity.Team;
import com.querydsl.core.repository.member.MemberRepository;
import com.querydsl.core.repository.team.TeamRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.web.querydsl.dto.MemberDto;
import com.querydsl.web.querydsl.dto.MemberTeamDto;
import com.querydsl.web.querydsl.dto.TeamDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
public class QueryTest {
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager em;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("배치사이즈 조절")
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
    @DisplayName("직접 dto로 변경")
    void test2() {
        Team teamA = teamRepository.save(new Team("teamA"));
        Team teamB = teamRepository.save(new Team("teamB"));

        memberRepository.save(new Member("userA", 10, teamA));
        memberRepository.save(new Member("userB", 20, teamB));
        memberRepository.save(new Member("userC", 30, teamA));

        em.flush();
        em.clear();

        long start = System.currentTimeMillis();

        QMember member = QMember.member;
        QTeam team = QTeam.team;

        List<Tuple> tuples = queryFactory
                .select(member, member.team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.deleteYn.eq("N"))
                .fetch();

        List<MemberDto> memberList = new ArrayList<>();
        for (Tuple tuple : tuples) {
            Team t = tuple.get(member.team);
            memberList.add(new MemberDto(tuple.get(member), new TeamDto(t.getId(), t.getName())));
        }

        long end = System.currentTimeMillis();
        for (MemberDto memberDto : memberList) {
            log.info("memberDto={}",memberDto);
        }

        log.info("time={}ms", end-start);

        assertThat(memberList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("dto 로 조회")
    void test3() {
        Team teamA = teamRepository.save(new Team("teamA"));
        Team teamB = teamRepository.save(new Team("teamB"));

        memberRepository.save(new Member("userA", 10, teamA));
        memberRepository.save(new Member("userB", 20, teamB));
        memberRepository.save(new Member("userC", 30, teamA));

        em.flush();
        em.clear();
        long start = System.currentTimeMillis();

        QMember member = QMember.member;
        QTeam team = QTeam.team;

        List<MemberTeamDto> result = queryFactory
                .select(Projections.fields(MemberTeamDto.class,
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .on(team.deleteYn.eq("N"))
                .fetch();

        long end = System.currentTimeMillis();

        for (MemberTeamDto memberTeamDto : result) {
            log.info("memberTeamDto={}", memberTeamDto);
        }
        log.info("time={}ms", end-start);

        assertThat(result.size()).isEqualTo(3);
    }
}
