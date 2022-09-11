package com.querydsl.web.controller;

import com.querydsl.core.entity.Member;
import com.querydsl.core.entity.Team;
import com.querydsl.web.dto.JoinMemberDto;
import com.querydsl.web.dto.MemberDto;
import com.querydsl.web.dto.TeamDto;
import com.querydsl.web.repository.dto.MemberTeamDto;
import com.querydsl.web.service.MemberService;
import com.querydsl.web.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/v1/members/create")
    public String createMember(Model model) {
        List<Team> teams = teamService.getTeamList();
        model.addAttribute("member", new JoinMemberDto());
        model.addAttribute("teams", teams);
        return "create";
    }

    @PostMapping("/v1/members/create")
    public String createMemberPost(@ModelAttribute("member") JoinMemberDto memberDto) {
        memberService.join(memberDto);
        return "redirect:/v1/members";
    }

    @GetMapping("/v1/members")
    public String getMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "memberList";
    }

    @GetMapping("/v1/members/{userId}")
    public String getMember(Model model, @PathVariable("userId") Long userId) {
        List<Team> teams = teamService.getTeamList();
        MemberTeamDto memberTeamDto = memberService.findByIdFetchTeam(userId);
        model.addAttribute("member", memberTeamDto);
        model.addAttribute("teams", teams);
        return "memberDetail";
    }

    @PostMapping("/v1/members/{userId}")
    public String updateMember(@ModelAttribute("member") MemberTeamDto member,  @PathVariable("userId") Long userId) {
        memberService.update(new MemberDto(userId, member.getUsername(), member.getAge()), member.getTeamId());
        return "redirect:/v1/members/";
    }

}
