package com.EntertainmentViet.backend.features.booking.dao;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class BookingRepositoryImpl extends BaseRepositoryImpl<Booking, Long> implements BookingRepository {

  private final BookingPredicate bookingPredicate;

  public BookingRepositoryImpl(EntityManager em, BookingPredicate bookingPredicate) {
    super(Booking.class, em);
    this.bookingPredicate = bookingPredicate;
  }

  @Override
  public Optional<Booking> findByUid(UUID uid) {
    QueryUtils.Root<Booking> root = QueryUtils.createRoot();

    var queryRoot = root.base(bookingPredicate.getRootBase(queryFactory))
            .joinPaths(bookingPredicate.joinAll())
            .build();

    QueryUtils.Query<Booking> query = QueryUtils.createQuery();
    return query.root(queryRoot)
            .predicates(bookingPredicate.uidEqual(uid))
            .get();
  }
}
