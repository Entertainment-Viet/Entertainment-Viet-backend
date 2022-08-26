package com.EntertainmentViet.backend.features.talent.dao;

import com.EntertainmentViet.backend.domain.entities.admin.QTalentFeedback;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QReview;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.booking.boundary.JobDetailPredicate;
import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.querydsl.core.types.Predicate;
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
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;


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

  public Predicate joinAll(JPAQueryFactory queryFactory) {

    // join bookings
    var talents = queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.bookings, booking).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(booking.organizer, QOrganizer.organizer).fetchJoin()
        .fetch();

    // join review
    talents = queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.reviews, review).fetchJoin()
        .where(talent.in(talents))
        .fetch();

    // join talentFeedback
    queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.feedbacks, feedback).fetchJoin()
        .where(talent.in(talents))
        .fetch();

    return null;


//    return query -> QueryUtils.combineJoinExpressionFrom(query,
//            joinBooking(),
//            joinReview(),
//            joinFeedBack()
//    );
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talent.uid.eq(uid);
  }
}
