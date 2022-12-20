package com.EntertainmentViet.backend.features.organizer.dao.organizer;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class OrganizerRepositoryImpl extends BaseRepositoryImpl<Organizer, Long> implements OrganizerRepository {

  private final QOrganizer organizer = QOrganizer.organizer;

  private final OrganizerPredicate organizerPredicate;

  public OrganizerRepositoryImpl(EntityManager em, OrganizerPredicate organizerPredicate) {
    super(Organizer.class, em);
    this.organizerPredicate = organizerPredicate;
  }

  @Override
  public Optional<Organizer> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(organizer)
        .where(ExpressionUtils.allOf(
            organizerPredicate.joinAll(queryFactory),
            organizerPredicate.uidEqual(uid))
        )
        .fetchOne());
  }

  @Override
  public Page<Organizer> findAll(Pageable pageable) {
    var organizerList = Optional.ofNullable(queryFactory.selectFrom(organizer)
        .where(organizerPredicate.joinAll(queryFactory))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(getSortedColumn(pageable.getSort(), Organizer.class))
        .fetch())
        .orElse(Collections.emptyList());

    return new PageImpl<>(organizerList, pageable, organizerList.size());
  }
}
