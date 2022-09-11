package com.querydsl.web.repository.team;

import com.querydsl.core.entity.Team;
import com.querydsl.core.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitTeam {

    private final InitTeamService initTeamService;

    @PostConstruct
    public void init() {
        initTeamService.init();
    }

    @Component
    static class InitTeamService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            for (int i = 0 ; i < 10 ; i++) {
                em.persist(new Team("team".concat(String.valueOf(i+1))));
            }
        }
    }
}
