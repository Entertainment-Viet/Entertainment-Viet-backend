package com.EntertainmentViet.backend.features.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
@Transactional
public interface BaseRepository<T> extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {
}
