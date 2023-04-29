package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.talent.TalentCategory;
import com.EntertainmentViet.backend.features.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TalentCategoryRepository extends BaseRepository<TalentCategory, Long> {

  List<TalentCategory> findByTalentId(UUID uid);
  Optional<TalentCategory> findByTalentIdAndCategoryId(UUID talentUid, UUID CategoryUid);
}
