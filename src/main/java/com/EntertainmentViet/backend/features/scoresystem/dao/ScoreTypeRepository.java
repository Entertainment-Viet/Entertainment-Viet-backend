package com.EntertainmentViet.backend.features.scoresystem.dao;

import com.EntertainmentViet.backend.domain.entities.talent.ScoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ScoreTypeRepository extends JpaRepository<ScoreType, Long> {

  Optional<ScoreType> findByName(String name);
}
