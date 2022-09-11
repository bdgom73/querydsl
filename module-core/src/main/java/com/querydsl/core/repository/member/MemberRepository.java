package com.querydsl.core.repository.member;

import com.querydsl.core.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{

}
