package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends IdentifiableRepository<Review> {

  Page<Review> findByTalentId(UUID talentUid, Pageable pageable);
}
