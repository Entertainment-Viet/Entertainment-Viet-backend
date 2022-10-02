package com.EntertainmentViet.backend.features.booking.dao.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookingPredicate extends IdentifiablePredicate<Booking> {

  private final QTalent talent = QTalent.talent;
  private final QOrganizer organizer = QOrganizer.organizer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QBooking booking = QBooking.booking;
  private final QCategory category = QCategory.category;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(booking).distinct()
        .leftJoin(booking.talent, talent).fetchJoin()
        .leftJoin(booking.organizer, organizer).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromOrganizerParams(ListOrganizerBookingParamDto paramDto) {
    var predicate = defaultPredicate();

    // List all booking have performanceStartTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.before(paramDto.getStartTime()).not()
      );
    // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.after(paramDto.getEndTime()).not()
      );
    // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.before(paramDto.getStartTime()).not()
              .and(booking.jobDetail.performanceEndTime.after(paramDto.getEndTime()).not())
      );
    }
    if (paramDto.getPaid() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getPaid() ? booking.isPaid.isTrue() : booking.isPaid.isFalse()
      );
    }
    if (paramDto.getStatus() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.status.eq(BookingStatus.ofI18nKey(paramDto.getStatus()))
      );
    }
    if (paramDto.getPaymentType() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.paymentType.eq(PaymentType.ofI18nKey(paramDto.getPaymentType()))
      );
    }
    if (paramDto.getTalent() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.talent.displayName.eq(paramDto.getTalent())
      );
    }
    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.category.uid.eq(paramDto.getCategory())
      );
    }
    if (paramDto.getWorkType() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.workType.eq(WorkType.ofI18nKey(paramDto.getWorkType()))
      );
    }
    return predicate;
  }

  public Predicate fromTalentParams(ListTalentBookingParamDto paramDto) {
    var predicate = defaultPredicate();

    // List all booking have performanceStartTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.before(paramDto.getStartTime()).not()
      );
      // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.after(paramDto.getEndTime()).not()
      );
      // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.before(paramDto.getStartTime()).not()
              .and(booking.jobDetail.performanceEndTime.after(paramDto.getEndTime()).not())
      );
    }
    if (paramDto.getPaid() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getPaid() ? booking.isPaid.isTrue() : booking.isPaid.isFalse()
      );
    }
    if (paramDto.getStatus() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.status.eq(BookingStatus.ofI18nKey(paramDto.getStatus()))
      );
    }
    if (paramDto.getPaymentType() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.paymentType.eq(PaymentType.ofI18nKey(paramDto.getPaymentType()))
      );
    }
    if (paramDto.getOrganizer() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.organizer.displayName.eq(paramDto.getOrganizer())
      );
    }
    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.category.uid.eq(paramDto.getCategory())
      );
    }
    if (paramDto.getWorkType() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.workType.eq(WorkType.ofI18nKey(paramDto.getWorkType()))
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return booking.uid.eq(uid);
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
    return booking.organizer.uid.eq(uid);
  }

  public BooleanExpression belongToTalent(UUID uid) {
    return booking.talent.uid.eq(uid);
  }
}
