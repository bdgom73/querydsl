package com.querydsl.web.service;

import com.querydsl.core.entity.Team;
import com.querydsl.core.repository.team.TeamRepository;
import com.querydsl.web.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public List<Team> getTeamList() {
        return teamRepository.findAllByNotDelete();
    }

    public Team findById(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow();
    }

    @Transactional
    public void updateTeam(Long teamId, TeamDto teamDto) {
        Team findTeam = findById(teamId);
        findTeam.changeName(teamDto.getName());
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        Team findTeam = findById(teamId);
        findTeam.deleteTeam();
    }
}
