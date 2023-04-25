package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.talent.QReview;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class ReviewRepositoryImpl extends BaseRepositoryImpl<Review, Long> implements ReviewRepository {

  private final ReviewPredicate reviewPredicate;

  private final QReview review = QReview.review;

  public ReviewRepositoryImpl(EntityManager em, ReviewPredicate reviewPredicate) {
    super(Review.class, em);
    this.reviewPredicate = reviewPredicate;
  }

  @Override
  public Optional<Review> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(review)
        .where(ExpressionUtils.allOf(
            reviewPredicate.joinAll(queryFactory),
            reviewPredicate.uidEqual(uid))
        )
        .fetchOne());
  }

  @Override
  public Page<Review> findByTalentId(UUID talentUid, Pageable pageable) {
    var reviewList = Optional.ofNullable(queryFactory.selectFrom(review)
        .where(ExpressionUtils.allOf(
            reviewPredicate.joinAll(queryFactory),
            reviewPredicate.belongToTalent(talentUid),
            reviewPredicate.isTalentArchived(false)
        ))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(review.createdAt.desc())
        .orderBy(getSortedColumn(pageable.getSort(), Review.class))
        .fetch())
        .orElse(Collections.emptyList());

    return new PageImpl<>(reviewList, pageable, reviewList.size());
  }
}
