package com.EntertainmentViet.backend.features.booking.dao;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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
            bookingPredicate.uidEqual(uid))
        )
        .fetchOne());
  }
}
