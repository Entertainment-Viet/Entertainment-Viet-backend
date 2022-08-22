package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;

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
    QueryUtils.Root<Organizer> root = QueryUtils.createRoot();

    var queryRoot = root.base(organizerPredicate.getRootBase(queryFactory))
        .joinPaths(organizerPredicate.joinAll())
        .build();

    QueryUtils.Query<Organizer> query = QueryUtils.createQuery();

    return query.root(queryRoot)
        .predicates(organizerPredicate.uidEqual(uid))
        .get();
  }
}
