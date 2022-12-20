package com.EntertainmentViet.backend.features.talent.dao.talent;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.entities.admin.QTalentFeedback;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QPriorityScore;
import com.EntertainmentViet.backend.domain.entities.talent.QReview;
import com.EntertainmentViet.backend.domain.entities.talent.QScoreType;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.QTalentDetail;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
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

@Component
@RequiredArgsConstructor
public class TalentPredicate extends IdentifiablePredicate<Talent> {

  private final QTalent talent = QTalent.talent;
  private final QTalentDetail talentDetail = QTalentDetail.talentDetail;
  private final QReview review = QReview.review;
  private final QTalentFeedback feedback = QTalentFeedback.talentFeedback;
  private final QBooking booking = QBooking.booking;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;
  private final QPackage aPackage = QPackage.package$;
  private final QPriorityScore priorityScore = QPriorityScore.priorityScore;
  private final QScoreType scoreType = QScoreType.scoreType;
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parentLocation");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");
  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {

    // join bookings
    var talents = queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.talentDetail, talentDetail).fetchJoin()
        .leftJoin(talentDetail.address, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(talent.bookings, booking).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(booking.organizer, QOrganizer.organizer).fetchJoin()
        .fetch();

    // join packages
    talents = queryFactory.selectFrom(talent).distinct()
            .leftJoin(talent.packages, aPackage).fetchJoin()
            .leftJoin(aPackage.jobDetail, jobDetail).fetchJoin()
            .leftJoin(jobDetail.category, category).fetchJoin()
            .leftJoin(jobDetail.location, location).fetchJoin()
            .leftJoin(location.type(), locationType).fetchJoin()
            .leftJoin(location.parent(), parentLocation).fetchJoin()
            .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
            .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
            .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
            .leftJoin(aPackage.orders, booking).fetchJoin()
            .where(talent.in(talents))
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
    talents = queryFactory.selectFrom(talent).distinct()
            .leftJoin(talent.offerCategories, category).fetchJoin()
            .leftJoin(category.parent).fetchJoin()
            .where(talent.in(talents))
            .fetch();

    // join priorityScores
    queryFactory.selectFrom(talent).distinct()
        .leftJoin(talent.priorityScores, priorityScore).fetchJoin()
        .leftJoin(priorityScore.scoreType, scoreType).fetchJoin()
        .where(talent.in(talents))
        .fetch();
    return null;
  }

  public Predicate fromParams(ListTalentParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto == null) {
      return predicate;
    }

    if (paramDto.getDisplayName() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.displayName.like("%" + paramDto.getDisplayName() + "%")
      );
    }

    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.offerCategories.any().in(JPAExpressions.selectFrom(category).where(category.uid.eq(paramDto.getCategory())))
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
    }
    else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
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
    }
    else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      // Get talent that don't have any booking occur during period from startTime to endTime
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.bookings.any().in(
                      JPAExpressions.selectFrom(booking).where(
                              booking.jobDetail.performanceStartTime.between(paramDto.getStartTime(), paramDto.getEndTime())
                                      .or(booking.jobDetail.performanceEndTime.between(paramDto.getStartTime(),
                                              paramDto.getEndTime()))
                      )
              ).not()
      );
    }

    if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() == null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.currency.eq(Currency.ofI18nKey(paramDto.getCurrency())))),
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.max.loe(paramDto.getMaxPrice()
                              )))
      );
    }
    else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() == null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.currency.eq(Currency.ofI18nKey(paramDto.getCurrency())))),
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.min.goe(paramDto.getMinPrice()
                              )))
      );
    }
    else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.currency.eq(Currency.ofI18nKey(paramDto.getCurrency())))),
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.min.goe(paramDto.getMinPrice()
                              ))),
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.price.max.loe(paramDto.getMaxPrice()
                              )))
      );
    }
    if (paramDto.getLocationName() != null && paramDto.getLocationType() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.location.type().type.likeIgnoreCase("%" + paramDto.getLocationType() + "%"))),
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.location.name.likeIgnoreCase("%" + paramDto.getLocationName() + "%")))
      );
    }
    if (paramDto.getLocationParentName() != null && paramDto.getLocationParentType() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.location.parent().type().type.likeIgnoreCase("%" + paramDto.getLocationParentType() + "%"))),
              talent.packages.any().in(
                      JPAExpressions.selectFrom(aPackage).where(
                              aPackage.jobDetail.location.parent().name.likeIgnoreCase("%" + paramDto.getLocationParentName() + "%")))
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talent.uid.eq(uid);
  }

  public BooleanExpression isArchived() {
        return talent.archived.isTrue();
  }
}
