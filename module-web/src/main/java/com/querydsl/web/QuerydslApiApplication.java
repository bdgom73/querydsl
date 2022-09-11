package com.querydsl.web;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@EntityScan(basePackages = {"com.querydsl.core.entity"})
@EnableJpaRepositories(basePackages = {"com.querydsl.core"})
@SpringBootApplication
public class QuerydslApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuerydslApiApplication.class, args);
	}

	@Bean
	public JPAQueryFactory queryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}
}
