package com.EntertainmentViet.backend.features.booking.dao;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.features.booking.boundary.JobDetailPredicate;
import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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

  private final JobDetailPredicate jobDetailPredicate;

  public JoinExpression joinTalent() {
    return query -> query.leftJoin(booking.talent, talent).fetchJoin();
  }

  public JoinExpression joinOrganizer () {
    return query -> query.leftJoin(booking.organizer, organizer).fetchJoin();
  }

  public JoinExpression  joinJobDetail() {
    return query -> query.leftJoin(booking.jobDetail, jobDetail).fetchJoin();
  }

  @Override
  public JPAQuery<Booking> getRootBase(JPAQueryFactory queryFactory) {
    return queryFactory.selectFrom(booking);
  }

  @Override
  public JoinExpression joinAll() {
    return query -> QueryUtils.combineJoinExpressionFrom(query,
            joinTalent(),
            joinOrganizer(),
            joinJobDetail(),
            jobDetailPredicate.joinAll()
    );
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return booking.uid.eq(uid);
  }
}
