package com.EntertainmentViet.backend.features.organizer.dao.organizer;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.*;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ListOrganizerParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrganizerPredicate extends IdentifiablePredicate<Organizer> {

  private final QOrganizer organizer = QOrganizer.organizer;
  private final QOrganizerDetail organizerDetail = QOrganizerDetail.organizerDetail;
  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QBooking booking = QBooking.booking;
  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parentCategory");
  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;
  private final QEvent event = QEvent.event;

  private final QOrganizerShoppingCart organizerShoppingCart = QOrganizerShoppingCart.organizerShoppingCart;

  private final QPackage aPackage = QPackage.package$;

  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parentLocation");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");
  private final QEventDetail eventDetail = QEventDetail.eventDetail;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    // join jobOffers
    var organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.organizerDetail, organizerDetail).fetchJoin()
        .leftJoin(organizerDetail.address, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(organizer.jobOffers, jobOffer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .fetch();

    // join bookings
    organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.bookings, booking).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(booking.talent, QTalent.talent).fetchJoin()
        .leftJoin(booking.talentPackage, aPackage).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    // join events
    organizers = queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.events, event).fetchJoin()
        .leftJoin(event.eventDetail, eventDetail).fetchJoin()
        .leftJoin(eventDetail.occurrenceAddress, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    queryFactory.select(event).from(event).distinct()
        .leftJoin(event.openPositions, eventOpenPosition).fetchJoin()
        .leftJoin(eventOpenPosition.applicants, booking).fetchJoin()
        .where(event.organizer.in(organizers))
        .fetch();

    // join shoppingCart
    queryFactory.selectFrom(organizer).distinct()
        .leftJoin(organizer.shoppingCart, organizerShoppingCart).fetchJoin()
        .leftJoin(organizerShoppingCart.talentPackage, aPackage).fetchJoin()
        .leftJoin(aPackage.talent, QTalent.talent).fetchJoin()
        .leftJoin(aPackage.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .where(organizer.in(organizers))
        .fetch();

    return null;
  }

  public Predicate fromParams(ListOrganizerParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto == null) {
      return predicate;
    }

    if (paramDto.getDisplayName() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          organizer.displayName.like("%" + paramDto.getDisplayName() + "%")
      );
    }

    if (paramDto.getStartTime() != null && paramDto.getEndTime() == null) {
      // Get organizer don't have any booking occur at startTime
      predicate = ExpressionUtils.allOf(
          predicate,
          organizer.bookings.any().in(
              JPAExpressions.selectFrom(booking).where(
                  Expressions.asDateTime(paramDto.getStartTime())
                      .between(booking.jobDetail.performanceStartTime, booking.jobDetail.performanceEndTime)
                      .not()
              )
          )
      );
    } else if (paramDto.getStartTime() == null && paramDto.getEndTime() != null) {
      // Get talent don't have any booking occur at endTime
      predicate = ExpressionUtils.allOf(
          predicate,
          organizer.bookings.any().in(
              JPAExpressions.selectFrom(booking).where(
                  Expressions.asDateTime(paramDto.getEndTime())
                      .between(booking.jobDetail.performanceStartTime, booking.jobDetail.performanceEndTime)
                      .not()
              )
          )
      );
    } else if (paramDto.getStartTime() != null && paramDto.getEndTime() != null) {
      // Get talent that don't have any booking occur during period from startTime to endTime
      predicate = ExpressionUtils.allOf(
          predicate,
          organizer.bookings.any().in(
              JPAExpressions.selectFrom(booking).where(
                  booking.jobDetail.performanceStartTime.between(paramDto.getStartTime(), paramDto.getEndTime())
                      .or(booking.jobDetail.performanceEndTime.between(paramDto.getStartTime(),
                          paramDto.getEndTime()))
              )
          ).not()
      );
    }

    if (paramDto.getLocationId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          Expressions.booleanTemplate("check_location({0}, {1}) > 0", paramDto.getLocationId(), organizer.organizerDetail.address.uid)
      );
    }

    if (paramDto.getWithArchived() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getWithArchived() ? isArchived(true) : isArchived(false)
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return organizer.uid.eq(uid);
  }

  public BooleanExpression isArchived(boolean archived) {
    return organizer.archived.eq(archived);
  }
}
