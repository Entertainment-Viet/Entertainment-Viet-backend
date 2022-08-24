package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

public class OrganizerRepositoryImpl extends BaseRepositoryImpl<Organizer, Long> implements OrganizerRepository {

  private final OrganizerPredicate organizerPredicate;

  public OrganizerRepositoryImpl(EntityManager em, OrganizerPredicate organizerPredicate) {
    super(Organizer.class, em);
    this.organizerPredicate = organizerPredicate;
  }

  @Override
  public Optional<Organizer> findByUid(UUID uid) {
    QOrganizer organizer = QOrganizer.organizer;

    return Optional.ofNullable(queryFactory.selectFrom(organizer)
        .where(ExpressionUtils.allOf(
            organizerPredicate.joinAll(queryFactory),
            organizerPredicate.uidEqual(uid))
        )
        .fetchOne());
  }
}
