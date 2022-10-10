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
import com.EntertainmentViet.backend.features.talent.dto.talent.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
  public ReviewResponseDto findAll(UUID talentUid, Pageable pageable) {
    var reviewList = reviewRepository.findByTalentId(talentUid, pageable);
    var dataPage = RestUtils.toLazyLoadPageResponse(reviewList.map(reviewMapper::toDto));

    if (reviewRepository.findByTalentId(talentUid, pageable.next()).hasContent()) {
      dataPage.getPaging().setLast(false);
    }

    // Construct responseDto
    var reviewSum = reviewList.stream().findAny().map(Review::getTalent).map(Talent::getReviewSum).orElse(Collections.emptyList());
    var response = ReviewResponseDto.builder();
    if (!reviewSum.isEmpty()) {
      response.sumScore1(reviewSum.get(0))
          .sumScore2(reviewSum.get(1))
          .sumScore3(reviewSum.get(2))
          .sumScore4(reviewSum.get(3))
          .sumScore5(reviewSum.get(4));
    }
    return response.reviews(dataPage).build();
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

    if (!EntityValidationUtils.isTalentWithUid(talent, talentUid)) {
      return Optional.empty();
    }
    if (review.getOrganizer() == null) {
      log.warn(String.format("Can not find organizer with id '%s' that making the review for talent at id '%s'", reviewDto.getOrganizer(), talentUid));
      return Optional.empty();
    }

    talent.addReview(review);
    talentRepository.save(talent);
    return Optional.ofNullable(reviewRepository.save(review).getUid());
  }
}
