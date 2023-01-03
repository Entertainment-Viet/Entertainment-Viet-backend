package com.EntertainmentViet.backend.features.booking.dao.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QEvent;
import com.EntertainmentViet.backend.domain.entities.organizer.QEventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListEventBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
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
  private final QPackage aPackage = QPackage.package$;
  private final QBooking bookingOfOrganizer = new QBooking("bookingOfBooking");
  private final QBooking bookingOfTalent =  new QBooking("bookingOfTalent");
  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parentCategory");
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parentLocation");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");
  private final QEvent event = QEvent.event;
  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    var bookingList = queryFactory.selectFrom(booking).distinct()
        .leftJoin(booking.talent, talent).fetchJoin()
        .leftJoin(booking.talentPackage, aPackage).fetchJoin()
        .leftJoin(talent.bookings, bookingOfTalent).fetchJoin()
        .fetch();

    queryFactory.selectFrom(booking).distinct()
        .leftJoin(booking.organizer, organizer).fetchJoin()
        .leftJoin(organizer.bookings, bookingOfOrganizer).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .where(booking.in(bookingList))
        .fetch();

    return null;
  }

  public Predicate fromParams(AdminListBookingParamDto paramDto) {
    var predicate = defaultPredicate();

    // List all booking have performanceStartTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not()
      );
      // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
      );
      // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not(),
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
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
          booking.talent.displayName.like("%"+paramDto.getTalent()+"%")
      );
    }
    if (paramDto.getOrganizer() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.organizer.displayName.like("%"+paramDto.getOrganizer()+"%")
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
    if (paramDto.getBookingCode() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.bookingCode.like("%"+paramDto.getBookingCode()+"%")
      );
    }

    // hide bookings whose talent is archived
    if (paramDto.getWithArchived() == Boolean.FALSE) {
      predicate = ExpressionUtils.allOf(
          predicate,
          this.isOrganizerArchived().not()
      );
    }

    if (paramDto.getOrganizerId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          belongToOrganizer(paramDto.getOrganizerId())
      );
    }
    if (paramDto.getTalentId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          belongToTalent(paramDto.getTalentId())
      );
    }
    if (paramDto.getEventId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          belongToEvent(paramDto.getEventId())
      );
    }
    if (paramDto.getId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          uidEqual(paramDto.getId())
      );
    }
    return predicate;
  }

  public Predicate fromOrganizerParams(ListOrganizerBookingParamDto paramDto) {
    var predicate = defaultPredicate();

    // List all booking have performanceStartTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not()
      );
    // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
      );
    // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not(),
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
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
          booking.talent.displayName.like("%"+paramDto.getTalent()+"%")
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
    if (paramDto.getBookingCode() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.bookingCode.like("%"+paramDto.getBookingCode()+"%")
      );
    }

    // hide bookings whose talent is archived
    if (paramDto.getWithArchived() == Boolean.FALSE) {
      predicate = ExpressionUtils.allOf(
        predicate,
        this.isOrganizerArchived().not()
      );
    }
    return predicate;
  }

  public Predicate fromTalentParams(ListTalentBookingParamDto paramDto) {
    var predicate = defaultPredicate();

    // List all booking have performanceEndTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not()
      );
      // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
      );
      // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not(),
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
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
          booking.organizer.displayName.like("%"+paramDto.getOrganizer()+"%")
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
    if (paramDto.getBookingCode() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.bookingCode.like("%"+paramDto.getBookingCode()+"%")
      );
    }

    // hide bookings whose organizer is archived
    if (paramDto.getWithArchived() == Boolean.FALSE) {
      predicate = ExpressionUtils.allOf(
        predicate,
        this.isTalentArchived().not()
      );
    }
    return predicate;
  }

  public Predicate fromEventParams(ListEventBookingParamDto paramDto) {
    var predicate = defaultPredicate();

    // List all booking have performanceStartTime equal or after the paramDto.startTime
    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not()
      );
      // List all booking have performanceEndTime equal or before the paramDto.startTime
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
      );
      // List all booking have performance duration within paramDto.startTime and paramDto.endTime
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.jobDetail.performanceEndTime.before(paramDto.getStartTime()).not(),
          booking.jobDetail.performanceStartTime.after(paramDto.getEndTime()).not()
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
          booking.talent.displayName.like("%"+paramDto.getTalent()+"%")
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
    if (paramDto.getBookingCode() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          booking.bookingCode.like("%"+paramDto.getBookingCode()+"%")
      );
    }

    // hide bookings whose talent is archived
    if (paramDto.getWithArchived() == Boolean.FALSE) {
      predicate = ExpressionUtils.allOf(
          predicate,
          this.isOrganizerArchived().not()
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

  public BooleanExpression belongToEvent(UUID uid) {
    var bookingInEventQuery = JPAExpressions.select(booking.id)
        .from(event)
        .innerJoin(event.openPositions, eventOpenPosition)
        .innerJoin(eventOpenPosition.applicants, booking)
        .where(event.uid.eq(uid));
    return booking.id.in(bookingInEventQuery);
  }

  public BooleanExpression isTalentArchived() {
    return booking.talent.archived.isTrue();
  }

  public BooleanExpression isOrganizerArchived() {
    return booking.organizer.archived.isTrue();
  }
}
