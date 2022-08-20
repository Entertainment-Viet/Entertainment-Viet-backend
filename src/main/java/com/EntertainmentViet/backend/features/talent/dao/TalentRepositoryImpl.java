package com.EntertainmentViet.backend.features.talent.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferPredicate;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TalentRepositoryImpl extends BaseRepositoryImpl<Talent, Long> implements TalentRepository {

  private final TalentPredicate talentPredicate;

  public TalentRepositoryImpl(EntityManager em, TalentPredicate talentPredicate) {
    super(Talent.class, em);
    this.talentPredicate = talentPredicate;
  }

  @Override
  public Optional<Talent> findByUid(UUID uid) {
    QueryUtils.Root<Talent> root = QueryUtils.createRoot();

    var queryRoot = root.base(talentPredicate.getRootBase(queryFactory))
            .joinPaths(talentPredicate.joinAll())
            .build();

    QueryUtils.Query<Talent> query = QueryUtils.createQuery();
    return query.root(queryRoot)
            .predicates(talentPredicate.uidEqual(uid))
            .get();
  }
}
