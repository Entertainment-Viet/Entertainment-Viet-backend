package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.admin.QOrganizerFeedback;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.organizer.QEvent;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.features.booking.dao.BookingPredicate;
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
public class OrganizerPredicate extends IdentifiablePredicate<Organizer> {

  private final QOrganizer organizer = QOrganizer.organizer;
  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QEvent event = QEvent.event;
  private final QBooking booking = QBooking.booking;
  private final QOrganizerFeedback organizerFeedback = QOrganizerFeedback.organizerFeedback;

  private final JobOfferPredicate jobOfferPredicate;
  private final BookingPredicate bookingPredicate;

  public JoinExpression joinJobOffer() {
    return query -> query.leftJoin(organizer.jobOffers, jobOffer).fetchJoin();
  }

  public JoinExpression joinEvent() {
    return query -> query.leftJoin(organizer.events, event).fetchJoin();
  }

  public JoinExpression joinBooking() {
    return query -> query.leftJoin(organizer.bookings, booking).fetchJoin();
  }

  public JoinExpression joinOrganizerFeedback() {
    return query -> query.leftJoin(organizer.feedbacks, organizerFeedback).fetchJoin();
  }

  public JoinExpression joinShoppable() {
    return query -> query.leftJoin(organizer.shoppables).fetchJoin();
  }

  @Override
  public JPAQuery<Organizer> getRootBase(JPAQueryFactory queryFactory) {
    return queryFactory.selectFrom(organizer);
  }

  @Override
  public JoinExpression joinAll() {
    return query -> QueryUtils.combineJoinExpressionFrom(query,
        joinJobOffer(),
        joinEvent(),
//        eventPredicate.joinAll(), //TODO implement predicate first
        joinBooking(),
        joinOrganizerFeedback(),
//        organizerFeedbackPredicate.joinAll(),  //TODO implement predicate first
        jobOfferPredicate.joinAll(),
        bookingPredicate.joinAll(),
        joinShoppable()
    );
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return organizer.uid.eq(uid);
  }
}
