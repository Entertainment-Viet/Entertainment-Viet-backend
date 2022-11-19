package com.EntertainmentViet.backend.features.organizer.dao.event;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.QEvent;
import com.EntertainmentViet.backend.domain.entities.organizer.QEventDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QEventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parent");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(event).distinct()
            .leftJoin(event.eventDetail, eventDetail).fetchJoin()
            .leftJoin(eventDetail.occurrenceAddress, location).fetchJoin()
            .leftJoin(location.locationType(), locationType).fetchJoin()
            .leftJoin(location.parent(), parentLocation).fetchJoin()
            .leftJoin(parentLocation.locationType(), parentLocationType).fetchJoin()
            .leftJoin(event.organizer, organizer).fetchJoin()
        .leftJoin(event.openPositions, eventOpenPosition).fetchJoin()
        .leftJoin(eventOpenPosition.jobOffer, jobOffer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(eventOpenPosition.applicants, booking).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromParams(ListEventParamDto paramDto) {
    var predicate = defaultPredicate();
    if (paramDto.getName() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.name.like("%"+paramDto.getName()+"%")
      );
    }
    if (paramDto.getIsActive() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getIsActive() ? event.isActive.isTrue() : event.isActive.isFalse()
      );
    }
    if (paramDto.getOrganizer() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.organizer.displayName.like("%"+paramDto.getOrganizer()+"%")
      );
    }
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      // Get event occurrenceTime is equal or after start time
      predicate = ExpressionUtils.allOf(
          predicate,
          event.eventDetail.occurrenceStartTime.before(paramDto.getStartTime()).not()
      );
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      // Get event occurrenceTime is equal or before end  time
      predicate = ExpressionUtils.allOf(
              predicate,
              event.eventDetail.occurrenceEndTime.after(paramDto.getEndTime()).not()
      );
    }
    else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      // Get event occurrenceTime is between start time and end time
      predicate = ExpressionUtils.allOf(
              predicate,
              event.eventDetail.occurrenceStartTime.before(paramDto.getStartTime()).not(),
              event.eventDetail.occurrenceEndTime.after(paramDto.getEndTime()).not()
      );
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
                              eventOpenPosition.jobOffer.jobDetail.price.max.loe(paramDto.getMaxPrice()
                              )))
      );
    }
    else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() == null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.currency
                                      .eq(Currency.ofI18nKey(paramDto.getCurrency())))),
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.min.goe(paramDto.getMinPrice()
                              )))
      );
    }
    else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.currency
                                      .eq(Currency.ofI18nKey(paramDto.getCurrency())))),
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.min.goe(paramDto.getMinPrice()
                              ))),
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.max.loe(paramDto.getMaxPrice()
                              )))
      );
    }
    if (paramDto.getLocationName() != null && paramDto.getLocationType() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              event.eventDetail.occurrenceAddress.locationType().type.likeIgnoreCase("%" + paramDto.getLocationType() + "%"),
              event.eventDetail.occurrenceAddress.name.likeIgnoreCase("%" + paramDto.getLocationName() + "%"));
    }
    if (paramDto.getParentName() != null && paramDto.getParentType() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              event.eventDetail.occurrenceAddress.parent().locationType().type.likeIgnoreCase("%" + paramDto.getParentType() + "%"),
              event.eventDetail.occurrenceAddress.parent().name.likeIgnoreCase("%" + paramDto.getParentName() + "%"));
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return event.uid.eq(uid);
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
    return event.organizer.uid.eq(uid);
  }

}
