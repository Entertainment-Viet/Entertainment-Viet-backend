package com.EntertainmentViet.backend.features.booking.dao.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
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

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return booking.uid.eq(uid);
  }
}
