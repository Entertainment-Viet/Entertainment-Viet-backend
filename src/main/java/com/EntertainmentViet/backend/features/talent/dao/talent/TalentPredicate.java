package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.admin.QTalentFeedback;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QReview;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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

  private final QPackage aPackage = QPackage.package$;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {

    // join bookings
    var talents = queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.bookings, booking).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(booking.organizer, QOrganizer.organizer).fetchJoin()
        .fetch();

    // join packages
    talents = queryFactory.selectFrom(talent).distinct()
            .leftJoin(talent.packages, aPackage).fetchJoin()
            .leftJoin(aPackage.jobDetail, jobDetail).fetchJoin()
            .leftJoin(jobDetail.category, category).fetchJoin()
            .leftJoin(aPackage.orders, booking).fetchJoin()
            .fetch();

    // join review
    talents = queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.reviews, review).fetchJoin()
        .where(talent.in(talents))
        .fetch();

    // join talentFeedback
    talents = queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.feedbacks, feedback).fetchJoin()
        .where(talent.in(talents))
        .fetch();

    // join offerCategories
    queryFactory.selectFrom(talent).distinct()
            .leftJoin(talent.offerCategories, category).fetchJoin()
            .leftJoin(category.parent).fetchJoin()
            .where(talent.in(talents))
            .fetch();

    return null;
  }

  public Predicate fromParams(ListTalentParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto.getDisplayName() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.displayName.eq(paramDto.getDisplayName())
      );
    }

    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.offerCategories.any().in(JPAExpressions.selectFrom(category).where(category.name.eq(paramDto.getCategory())))
      );
    }

    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      // Get talent don't have any booking occur at startTime
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.bookings.any().in(
                      JPAExpressions.selectFrom(booking).where(
                              Expressions.asDateTime(paramDto.getStartTime())
                                      .between(booking.jobDetail.performanceStartTime, booking.jobDetail.performanceEndTime)
                                      .not()
                      )
              )
      );
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      // Get talent don't have any booking occur at endTime
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.bookings.any().in(
                      JPAExpressions.selectFrom(booking).where(
                              Expressions.asDateTime(paramDto.getEndTime())
                                      .between(booking.jobDetail.performanceStartTime, booking.jobDetail.performanceEndTime)
                                      .not()
                      )
              )
      );
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      // Get talent that don't have any booking occur during period from startTime to endTime
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.bookings.any().in(
                      JPAExpressions.selectFrom(booking).where(
                              booking.jobDetail.performanceStartTime.after(paramDto.getStartTime())
                              .or(booking.jobDetail.performanceEndTime.before(paramDto.getEndTime()))
                      )
              ).not()
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talent.uid.eq(uid);
  }
}
