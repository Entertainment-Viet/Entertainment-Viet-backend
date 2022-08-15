package com.EntertainmentViet.backend.features.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
@Transactional
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
}
