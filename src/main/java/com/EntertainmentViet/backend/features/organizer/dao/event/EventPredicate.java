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
import com.EntertainmentViet.backend.domain.values.LocationAddress;
import com.EntertainmentViet.backend.domain.values.QCategory;
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

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(event).distinct()
        .leftJoin(event.eventDetail, eventDetail).fetchJoin()
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

    if (paramDto.getMax() != null && paramDto.getMin() == null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.currency.eq(Currency.valueOf(paramDto.getCurrency())
                              ))),
              event.openPositions.any().in(
                      JPAExpressions.selectFrom(eventOpenPosition).where(
                              eventOpenPosition.jobOffer.jobDetail.price.max.eq(paramDto.getMax()
                              )))
      );
    }
    else if (paramDto.getMax() == null && paramDto.getMin() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              event.openPositions.any().jobOffer.jobDetail.price.currency.stringValue().eq(paramDto.getCurrency()),
              event.openPositions.any().jobOffer.jobDetail.price.min.goe(paramDto.getMin())
      );
    }
    else if (paramDto.getMax() != null && paramDto.getMin() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              jobDetail.price.currency.stringValue().eq(paramDto.getCurrency()),
              jobDetail.price.min.goe(paramDto.getMin()),
              jobDetail.price.max.loe(paramDto.getMax())
      );
    }

    if (paramDto.getCity() != null && paramDto.getDistrict() != null &&
            paramDto.getStreet() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .city(paramDto.getCity())
                              .district(paramDto.getDistrict())
                              .street(paramDto.getStreet())
                              .build()
              )
      );
    }
    else if (paramDto.getCity() != null && paramDto.getDistrict() != null &&
            paramDto.getStreet() == null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .city(paramDto.getCity())
                              .district(paramDto.getDistrict())
                              .build()
              )
      );
    }

    else if (paramDto.getCity() == null && paramDto.getDistrict() != null &&
            paramDto.getStreet() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .district(paramDto.getDistrict())
                              .street(paramDto.getStreet())
                              .build()
              )
      );
    }
    else if (paramDto.getCity() != null && paramDto.getDistrict() == null &&
            paramDto.getStreet() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .city(paramDto.getCity())
                              .street(paramDto.getStreet())
                              .build()
              )
      );
    }
    else if (paramDto.getCity() != null && paramDto.getDistrict() == null &&
            paramDto.getStreet() == null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .city(paramDto.getCity())
                              .build()
              )
      );
    }
    else if (paramDto.getCity() == null && paramDto.getDistrict() != null &&
            paramDto.getStreet() == null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .district(paramDto.getDistrict())
                              .build()
              )
      );
    }
    else if (paramDto.getCity() == null && paramDto.getDistrict() == null &&
            paramDto.getStreet() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              Expressions.booleanTemplate(
                      "json_contains_key({0}, {1})",
                      jobDetail.location,
                      LocationAddress.builder()
                              .street(paramDto.getStreet())
                              .build()
              )
      );
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
