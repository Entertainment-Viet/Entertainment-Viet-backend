package com.EntertainmentViet.backend.features.organizer.dao.event;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.QEvent;
import com.EntertainmentViet.backend.domain.entities.organizer.QEventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventPositionParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventOpenPositionPredicate extends IdentifiablePredicate<Event> {

  private final QEvent event = QEvent.event;
  private final QOrganizer organizer = QOrganizer.organizer;
  private final QTalent talent = QTalent.talent;
  private final QBooking booking = QBooking.booking;
  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;
  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parent");
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parent");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");
  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(eventOpenPosition).distinct()
        .leftJoin(eventOpenPosition.event, event).fetchJoin()
        .leftJoin(event.organizer, organizer).fetchJoin()
        .leftJoin(eventOpenPosition.jobOffer, jobOffer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobOffer.organizer, organizer).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(eventOpenPosition.applicants, booking).fetchJoin()
        .leftJoin(booking.talent, talent).fetchJoin()
        .leftJoin(booking.organizer, organizer).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromParams(ListEventPositionParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto == null) {
      return predicate;
    }

    if (paramDto.getEvent() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          event.name.like("%"+paramDto.getEvent()+"%")
      );
    }
    if (paramDto.getIsActive() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getIsActive() ? eventOpenPosition.jobOffer.isActive.isTrue() : eventOpenPosition.jobOffer.isActive.isFalse()
      );
    }
    if (paramDto.getQuantity() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          eventOpenPosition.quantity.eq(paramDto.getQuantity())
      );
    }
    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          eventOpenPosition.jobOffer.jobDetail.category.uid.eq(paramDto.getCategory())
      );
    }
    if (paramDto.getWorkType() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          eventOpenPosition.jobOffer.jobDetail.workType.eq(WorkType.ofI18nKey(paramDto.getWorkType()))
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return eventOpenPosition.uid.eq(uid);
  }

  public BooleanExpression isArchived() {
    return eventOpenPosition.archived.isTrue();
  }


  public Predicate belongToOrganizer(UUID organizerUid) {
    return eventOpenPosition.event.organizer.uid.eq(organizerUid);
  }

  public Predicate belongToEvent(UUID eventUid) {
    return eventOpenPosition.event.uid.eq(eventUid);
  }
}
