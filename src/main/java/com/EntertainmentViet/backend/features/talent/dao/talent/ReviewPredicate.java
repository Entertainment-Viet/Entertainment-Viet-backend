package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QReview;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewPredicate extends IdentifiablePredicate<Review> {

  private final QTalent talent = QTalent.talent;
  private final QReview review = QReview.review;
  private final QOrganizer organizer = QOrganizer.organizer;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(review).distinct()
        .leftJoin(review.talent, talent).fetchJoin()
        .leftJoin(review.organizer, organizer).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate belongToTalent(UUID talentUid) {
    return review.talent.uid.eq(talentUid);
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return review.uid.eq(uid);
  }
}
