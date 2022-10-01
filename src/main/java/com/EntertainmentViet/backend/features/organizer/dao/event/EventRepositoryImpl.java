package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.*;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
            eventPredicate.uidEqual(uid))
        )
        .fetchOne());  }

  @Override
  public List<Event> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(event)
        .where(ExpressionUtils.allOf(
            eventPredicate.joinAll(queryFactory),
            eventPredicate.belongToOrganizer(uid),
            eventPredicate.fromParams(paramDto)
        ))
        .orderBy(getSortedColumn(pageable.getSort(), Event.class))
        .fetch();
  }
}
