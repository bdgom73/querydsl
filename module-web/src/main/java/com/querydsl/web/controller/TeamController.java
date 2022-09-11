package com.querydsl.web.controller;

import com.querydsl.core.entity.Team;
import com.querydsl.web.dto.TeamDto;
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
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/v1/teams")
    public String getTeams(Model model) {
        List<Team> teamList = teamService.getTeamList();
        model.addAttribute("teams", teamList);
        return "teamList";
    }

    @GetMapping("/v1/teams/{teamId}")
    public String getTeam(@PathVariable("teamId") Long teamId, Model model) {
        Team team = teamService.findById(teamId);
        model.addAttribute("team", new TeamDto(team.getId(), team.getName()));
        return "teamDetail";
    }

    @PostMapping("/v1/teams/{teamId}")
    public String updateTeam(@PathVariable("teamId") Long teamId, @ModelAttribute("team") TeamDto teamDto) {
        teamService.updateTeam(teamId, teamDto);
        return "redirect:/v1/teams/" + teamId;
    }

    @PostMapping("/v1/teams/{teamId}/delete")
    public String updateTeam(@PathVariable("teamId") Long teamId) {
        teamService.deleteTeam(teamId);
        return "redirect:/v1/teams/";
    }

}
