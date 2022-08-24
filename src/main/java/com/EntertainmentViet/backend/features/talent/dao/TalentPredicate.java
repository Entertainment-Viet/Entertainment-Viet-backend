package com.EntertainmentViet.backend.features.talent.dao;

import com.EntertainmentViet.backend.domain.entities.admin.QTalentFeedback;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.talent.QReview;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.booking.boundary.JobDetailPredicate;
import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TalentPredicate extends IdentifiablePredicate<Talent> {

  private final QTalent talent = QTalent.talent;
  private final QReview review = QReview.review;
  private final QTalentFeedback feedback = QTalentFeedback.talentFeedback;
  private final QBooking booking = QBooking.booking;


  public JoinExpression joinBooking() {
    return query -> query.leftJoin(talent.bookings, booking).fetchJoin();
  }

  public JoinExpression joinReview() {
    return query -> query.leftJoin(talent.reviews, review).fetchJoin();
  }

  public JoinExpression  joinFeedBack() {
    return query -> query.leftJoin(talent.feedbacks, feedback).fetchJoin();
  }

  public JPAQuery<Talent> getRootBase(JPAQueryFactory queryFactory) {
    return queryFactory.selectFrom(talent);
  }

  public JoinExpression joinAll() {
    return query -> QueryUtils.combineJoinExpressionFrom(query,
            joinBooking(),
            joinReview(),
            joinFeedBack()
    );
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talent.uid.eq(uid);
  }
}
