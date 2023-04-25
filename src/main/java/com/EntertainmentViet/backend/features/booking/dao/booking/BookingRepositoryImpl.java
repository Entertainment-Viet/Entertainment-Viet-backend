package com.EntertainmentViet.backend.features.booking.dao.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListEventBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class BookingRepositoryImpl extends BaseRepositoryImpl<Booking, Long> implements BookingRepository {

  private final QBooking booking = QBooking.booking;

  private final BookingPredicate bookingPredicate;

  public BookingRepositoryImpl(EntityManager em, BookingPredicate bookingPredicate) {
    super(Booking.class, em);
    this.bookingPredicate = bookingPredicate;
  }

  @Override
  public Optional<Booking> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(booking)
        .where(ExpressionUtils.allOf(
            bookingPredicate.joinAll(queryFactory),
            bookingPredicate.uidEqual(uid)))
        .fetchOne());
  }

  @Override
  public List<Booking> findAll(AdminListBookingParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(booking)
        .where(ExpressionUtils.allOf(
            bookingPredicate.joinAll(queryFactory),
            bookingPredicate.fromParams(paramDto)))
        .orderBy(getSortedColumn(pageable.getSort(), Booking.class))
        .fetch();
  }

  @Override
  public List<Booking> findByOrganizerUid(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(booking)
        .where(ExpressionUtils.allOf(
            bookingPredicate.joinAll(queryFactory),
            bookingPredicate.belongToOrganizer(organizerId),
            bookingPredicate.fromOrganizerParams(paramDto)))
        .orderBy(getSortedColumn(pageable.getSort(), Booking.class))
        .fetch();
  }

  @Override
  public List<Booking> findByTalentUid(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(booking)
        .where(ExpressionUtils.allOf(
            bookingPredicate.joinAll(queryFactory),
            bookingPredicate.belongToTalent(talentId),
            bookingPredicate.fromTalentParams(paramDto)))
        .orderBy(getSortedColumn(pageable.getSort(), Booking.class))
        .fetch();
  }

  @Override
  public List<Booking> findByEventUid(UUID eventId, ListEventBookingParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(booking)
        .where(ExpressionUtils.allOf(
            bookingPredicate.joinAll(queryFactory),
            bookingPredicate.belongToEvent(eventId),
            bookingPredicate.fromEventParams(paramDto)))
        .orderBy(getSortedColumn(pageable.getSort(), Booking.class))
        .fetch();
  }
}
