package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.*;
import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
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
public class EventPredicate extends IdentifiablePredicate<Event> {

  private final QEvent event = QEvent.event;
  private final QEventDetail eventDetail = QEventDetail.eventDetail;
  private final QOrganizer organizer = QOrganizer.organizer;
  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;
  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QBooking booking = QBooking.booking;

  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parentCategory");
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parentLocation");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(event).distinct()
        .leftJoin(event.eventDetail, eventDetail).fetchJoin()
        .leftJoin(eventDetail.occurrenceAddress, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(event.organizer, organizer).fetchJoin()
        .leftJoin(event.openPositions, eventOpenPosition).fetchJoin()
        .leftJoin(eventOpenPosition.jobOffer, jobOffer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(eventOpenPosition.applicants, booking).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromParams(ListEventParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto == null) {
      return predicate;
    }

    if (paramDto.getName() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.name.like("%" + paramDto.getName() + "%"));
    }
    if (paramDto.getIsActive() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getIsActive() ? event.isActive.isTrue() : event.isActive.isFalse());
    }
    if (paramDto.getOrganizer() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.organizer.displayName.like("%" + paramDto.getOrganizer() + "%"));
    }
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      // Get event occurrenceTime is equal or after start time
      predicate = ExpressionUtils.allOf(
          predicate,
          event.eventDetail.occurrenceStartTime.before(paramDto.getStartTime()).not());
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      // Get event occurrenceTime is equal or before end time
      predicate = ExpressionUtils.allOf(
          predicate,
          event.eventDetail.occurrenceEndTime.after(paramDto.getEndTime()).not());
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      // Get event occurrenceTime is between start time and end time
      predicate = ExpressionUtils.allOf(
          predicate,
          event.eventDetail.occurrenceStartTime.before(paramDto.getStartTime()).not(),
          event.eventDetail.occurrenceEndTime.after(paramDto.getEndTime()).not());
    }
    if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.currency
                      .eq(Currency.ofI18nKey(paramDto.getCurrency())))),
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.max.loe(paramDto.getMaxPrice()))));
    } else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() == null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.currency
                      .eq(Currency.ofI18nKey(paramDto.getCurrency())))),
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.min.goe(paramDto.getMinPrice()))));
    } else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.currency
                      .eq(Currency.ofI18nKey(paramDto.getCurrency())))),
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.min.goe(paramDto.getMinPrice()))),
          event.openPositions.any().in(
              JPAExpressions.selectFrom(eventOpenPosition).where(
                  eventOpenPosition.jobOffer.jobDetail.price.max.loe(paramDto.getMaxPrice()))));
    }

    if (paramDto.getLocationId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          Expressions.booleanTemplate("check_location({0}, {1}) > 0", paramDto.getLocationId(), event.eventDetail.occurrenceAddress.uid)
      );
    }

    if (paramDto.getWithArchived() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getWithArchived() ? isArchived(true).or(isOrganizerArchived(true)) :
            isArchived(false).and(isOrganizerArchived(false))
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return event.uid.eq(uid);
  }

  public BooleanExpression isArchived(boolean archived) {
    return event.archived.eq(archived);
  }

  public BooleanExpression isOrganizerArchived(boolean archived) {
    return event.organizer.archived.eq(archived);
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
    return event.organizer.uid.eq(uid);
  }
}
