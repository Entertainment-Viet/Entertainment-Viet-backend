package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.talent.QTalentCategory;
import com.EntertainmentViet.backend.domain.entities.talent.TalentCategory;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TalentCategoryRepositoryImpl extends BaseRepositoryImpl<TalentCategory, Long> implements TalentCategoryRepository {

  private final QTalentCategory talentCategory = QTalentCategory.talentCategory;

  private final TalentCategoryPredicate talentCategoryPredicate;

  public TalentCategoryRepositoryImpl(EntityManager em, TalentCategoryPredicate talentCategoryPredicate) {
    super(TalentCategory.class, em);
    this.talentCategoryPredicate = talentCategoryPredicate;
  }

  @Override
  public List<TalentCategory> findByTalentId(UUID uid) {
    return queryFactory.selectFrom(talentCategory)
        .where(ExpressionUtils.allOf(
            talentCategoryPredicate.joinAll(queryFactory),
            talentCategoryPredicate.belongToTalent(uid)
        ))
        .fetch();
  }

  @Override
  public Optional<TalentCategory> findByTalentIdAndCategoryId(UUID talentUid, UUID categoryUid) {
    return Optional.ofNullable(queryFactory.selectFrom(talentCategory)
        .where(ExpressionUtils.allOf(
            talentCategoryPredicate.joinAll(queryFactory),
            talentCategoryPredicate.belongToTalentAndHaveCategory(talentUid, categoryUid))
        )
        .fetchOne());
    }
}
