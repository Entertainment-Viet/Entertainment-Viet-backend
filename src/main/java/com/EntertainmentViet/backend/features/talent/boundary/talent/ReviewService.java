package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.ReviewRepository;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadReviewDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService implements ReviewBoundary {

  private final ReviewRepository reviewRepository;

  private final TalentRepository talentRepository;

  private final ReviewMapper reviewMapper;

  @Override
  public CustomPage<ReadReviewDto> findAll(UUID talentUid, Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        reviewRepository.findByTalentId(talentUid, pageable)
        .map(reviewMapper::toDto));

    if (reviewRepository.findByTalentId(talentUid, pageable.next()).hasContent()) {
      dataPage.getPaging().setLast(false);
    }

    return dataPage;
  }

  @Override
  public Optional<ReadReviewDto> findByUidAndTalentUid(UUID uid, UUID talentUid) {
    Review review =  reviewRepository.findByUid(uid).orElse(null);

    if (!EntityValidationUtils.isReviewWithUidExist(review, uid)) {
      return Optional.empty();
    }
    if (!EntityValidationUtils.isReviewBelongToTalentWithUid(review, talentUid)) {
      return Optional.empty();
    }

    return Optional.ofNullable(reviewMapper.toDto(review));
  }

  @Override
  public Optional<UUID> create(CreateReviewDto reviewDto, UUID talentUid) {
    Review review = reviewMapper.fromCreateToModel(reviewDto);
    Talent talent = talentRepository.findByUid(talentUid).orElse(null);
    review.setTalent(talent);

    if (!EntityValidationUtils.isReviewBelongToTalentWithUid(review, talentUid)) {
      return Optional.empty();
    }
    if (review.getOrganizer() == null) {
      log.warn(String.format("Can not find organizer with id '%s' that making the review for talent at id '%s'", reviewDto.getOrganizer(), talentUid));
      return Optional.empty();
    }

    return Optional.ofNullable(reviewRepository.save(review).getUid());
  }
}
