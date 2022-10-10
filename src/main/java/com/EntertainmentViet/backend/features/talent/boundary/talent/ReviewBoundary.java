package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadReviewDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReviewResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ReviewBoundary {
  ReviewResponseDto findAll(UUID talentUid, Pageable pageable);

  Optional<ReadReviewDto> findByUidAndTalentUid(UUID uid, UUID talentUid);

  Optional<UUID> create(CreateReviewDto reviewDto, UUID talentUid);
}
