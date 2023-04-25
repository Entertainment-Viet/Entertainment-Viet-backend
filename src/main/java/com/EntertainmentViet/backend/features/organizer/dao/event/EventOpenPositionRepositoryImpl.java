package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.QEventOpenPosition;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventPositionParamDto;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventOpenPositionRepositoryImpl extends BaseRepositoryImpl<EventOpenPosition, Long> implements EventOpenPositionRepository {

  private final QEventOpenPosition eventOpenPosition = QEventOpenPosition.eventOpenPosition;

  private final EventOpenPositionPredicate eventOpenPositionPredicate;

  public EventOpenPositionRepositoryImpl(EntityManager em, EventOpenPositionPredicate eventOpenPositionPredicate) {
    super(EventOpenPosition.class, em);
    this.eventOpenPositionPredicate = eventOpenPositionPredicate;
  }

  @Override
  public Optional<EventOpenPosition> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(eventOpenPosition)
        .where(ExpressionUtils.allOf(
            eventOpenPositionPredicate.joinAll(queryFactory),
            eventOpenPositionPredicate.uidEqual(uid))
        )
        .fetchOne());
  }

  @Override
  public List<EventOpenPosition> findByOrganizerUidAndEventUid(UUID organizerUid, UUID eventUid, ListEventPositionParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(eventOpenPosition)
        .where(ExpressionUtils.allOf(
            eventOpenPositionPredicate.joinAll(queryFactory),
            eventOpenPositionPredicate.belongToOrganizer(organizerUid),
            eventOpenPositionPredicate.belongToEvent(eventUid),
            eventOpenPositionPredicate.fromParams(paramDto)
        ))
        .orderBy(getSortedColumn(pageable.getSort(), EventOpenPosition.class))
        .fetch();
  }
}
