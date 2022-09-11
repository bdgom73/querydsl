package com.querydsl.web.service;

import com.querydsl.core.entity.Member;
import com.querydsl.core.entity.Team;
import com.querydsl.core.repository.member.MemberRepository;
import com.querydsl.core.repository.team.TeamRepository;
import com.querydsl.web.dto.JoinMemberDto;
import com.querydsl.web.dto.MemberDto;
import com.querydsl.web.repository.member.MemberQueryRepository;
import com.querydsl.web.repository.dto.MemberTeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final TeamRepository teamRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    public MemberTeamDto findByIdFetchTeam(Long teamId) {
        return memberQueryRepository.findByIdFetchTeam(teamId);
    }
    @Transactional
    public void join(JoinMemberDto memberDto) {
        memberRepository.findByUsername(memberDto.getUsername()).ifPresent(m -> {
            throw new IllegalStateException();
        });

        Team team = teamRepository.findById(memberDto.getTeamId()).orElseThrow();

        Member member = new Member(memberDto.getUsername(), memberDto.getAge(), team);
        memberRepository.save(member);
    }

    @Transactional
    public void update(MemberDto memberDto, Long teamId) {
        Member findMember = memberRepository.findByIdFetchTeam(memberDto.getId()).orElseThrow();

        if (teamId != null && !findMember.getTeam().getId().equals(teamId)) {
            Team team = teamRepository.findById(teamId).orElseThrow();
            findMember.changeTeam(team);
        }

        memberRepository.findByUsername(memberDto.getUsername()).ifPresent(m -> {
            if (!m.getId().equals(memberDto.getId())
                    && m.getUsername().equals(memberDto.getUsername())) {
                throw new IllegalStateException("이미 사용중인 username 입니다.");
            }
        });

        findMember.changeMember(memberDto.getUsername(), memberDto.getAge());
    }
}
