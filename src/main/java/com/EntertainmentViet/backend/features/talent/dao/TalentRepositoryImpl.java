package com.EntertainmentViet.backend.features.talent.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferPredicate;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferRepository;
import com.querydsl.core.types.ExpressionUtils;
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
    QTalent talent = QTalent.talent;

    return Optional.ofNullable(queryFactory.selectFrom(talent)
        .where(ExpressionUtils.allOf(
            talentPredicate.joinAll(queryFactory),
            talentPredicate.uidEqual(uid))
        )
        .fetchOne());
  }
}
