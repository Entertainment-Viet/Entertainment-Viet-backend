package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.QEvent;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryImpl extends BaseRepositoryImpl<Event, Long> implements EventRepository {

  private final QEvent event = QEvent.event;

  private final EventPredicate eventPredicate;

  public EventRepositoryImpl(EntityManager em, EventPredicate eventPredicate) {
    super(Event.class, em);
    this.eventPredicate = eventPredicate;
  }

  @Override
  public Optional<Event> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(event)
        .where(ExpressionUtils.allOf(
            eventPredicate.joinAll(queryFactory),
            eventPredicate.uidEqual(uid)),
            eventPredicate.isArchived(false))
        .fetchOne());
  }

  @Override
  public Page<Event> findAll(ListEventParamDto paramDto, Pageable pageable) {
    var eventList = Optional.ofNullable(queryFactory.selectFrom(event)
        .where(ExpressionUtils.allOf(
            eventPredicate.joinAll(queryFactory),
            eventPredicate.fromParams(paramDto)),
            eventPredicate.afterCurrentDay())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(getSortedColumn(pageable.getSort(), Talent.class))
        .fetch())
        .orElse(Collections.emptyList());

    return new PageImpl<>(eventList, pageable, eventList.size());
  }

  @Override
  public List<Event> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(event)
        .where(ExpressionUtils.allOf(
            eventPredicate.joinAll(queryFactory),
            eventPredicate.belongToOrganizer(uid),
            eventPredicate.fromParams(paramDto)),
            eventPredicate.afterCurrentDay())
        .orderBy(getSortedColumn(pageable.getSort(), Event.class))
        .fetch();
  }
}
