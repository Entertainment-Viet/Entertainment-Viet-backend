package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.admin.QOrganizerFeedback;
import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.organizer.QEvent;
import com.EntertainmentViet.backend.domain.entities.organizer.QEventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
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
public class OrganizerPredicate extends IdentifiablePredicate<Organizer> {

  private final QOrganizer organizer = QOrganizer.organizer;
  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QBooking booking = QBooking.booking;
  private final QCategory category = QCategory.category;
  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;
  private final QEvent event = QEvent.event;
  private final QOrganizerFeedback organizerFeedback = QOrganizerFeedback.organizerFeedback;

  public Predicate joinAll(JPAQueryFactory queryFactory) {
    // join jobOffers
    var organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.jobOffers, jobOffer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .fetch();

    // join bookings
    organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.bookings, booking).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(booking.talent, QTalent.talent).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    // join events
    organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.events, event).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    queryFactory.select(event).from(event).distinct()
        .leftJoin(event.openPositions, eventOpenPosition).fetchJoin()
        .leftJoin(eventOpenPosition.applicants, booking).fetchJoin()
        .where(event.organizer.in(organizers))
        .fetch();

    // join feedbacks
    organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.feedbacks, organizerFeedback).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    // join shoppingCart
    queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.shoppingCart).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    return null;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return organizer.uid.eq(uid);
  }
}
