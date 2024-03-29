package com.EntertainmentViet.backend.features.talent.dao.talent;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TalentRepositoryImpl extends BaseRepositoryImpl<Talent, Long> implements TalentRepository {

  private final QTalent talent = QTalent.talent;

  private final TalentPredicate talentPredicate;

  public TalentRepositoryImpl(EntityManager em, TalentPredicate talentPredicate) {
    super(Talent.class, em);
    this.talentPredicate = talentPredicate;
  }

  @Override
  public Optional<Talent> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(talent)
        .where(ExpressionUtils.allOf(
            talentPredicate.joinAll(queryFactory),
            talentPredicate.uidEqual(uid))
        )
        .fetchOne());
  }

  @Override
  public Page<Talent> findAll(ListTalentParamDto paramDto, Pageable pageable) {
    var talentList = Optional.ofNullable(queryFactory.selectFrom(talent)
            .where(ExpressionUtils.allOf(
                    talentPredicate.joinAll(queryFactory),
                    talentPredicate.fromParams(paramDto)
            ))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(talent.finalScore.desc())
            .orderBy(getSortedColumn(pageable.getSort(), Talent.class))
            .fetch())
            .orElse(Collections.emptyList());

    return new PageImpl<>(talentList, pageable, talentList.size());
  }
}
