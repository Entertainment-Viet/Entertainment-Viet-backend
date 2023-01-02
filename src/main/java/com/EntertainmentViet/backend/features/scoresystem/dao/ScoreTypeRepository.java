package com.EntertainmentViet.backend.features.scoresystem.dao;

import com.EntertainmentViet.backend.domain.entities.talent.ScoreType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface ScoreTypeRepository extends IdentifiableRepository<ScoreType> {

  Optional<ScoreType> findByName(String name);
}
