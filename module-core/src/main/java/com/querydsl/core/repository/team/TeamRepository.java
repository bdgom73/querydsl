package com.querydsl.core.repository.team;

import com.querydsl.core.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
}
