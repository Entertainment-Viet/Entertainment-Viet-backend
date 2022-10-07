package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.*;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventPredicate extends IdentifiablePredicate<Event> {

  private final QEvent event = QEvent.event;
  private final QOrganizer organizer = QOrganizer.organizer;
  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;
  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QBooking booking = QBooking.booking;
  private final QCategory category = QCategory.category;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(event).distinct()
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
          event.occurrenceTime.before(paramDto.getStartTime()).not()
      );
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      // Get event occurrenceTime is equal or before end  time
      predicate = ExpressionUtils.allOf(
          predicate,
          event.occurrenceTime.after(paramDto.getEndTime()).not()
      );
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      // Get event occurrenceTime is between start time and end time
      predicate = ExpressionUtils.allOf(
          predicate,
          event.occurrenceTime.between(paramDto.getStartTime(), paramDto.getEndTime())
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
