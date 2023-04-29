package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.entities.talent.QTalentCategory;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.BasePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TalentCategoryPredicate extends BasePredicate<OrganizerShoppingCart> {

  private final QTalentCategory talentCategory = QTalentCategory.talentCategory;
  private final QTalent talent = QTalent.talent;
  private final QCategory category = QCategory.category;

  private final QOrganizer organizer = QOrganizer.organizer;
  private final QPackage aPackage = QPackage.package$;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory parentCategory = new QCategory("parentCategory");
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parentLocation");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(talentCategory).distinct()
        .leftJoin(talentCategory.talent, talent).fetchJoin()
        .leftJoin(talentCategory.category, category).fetchJoin()
        .fetch();

    return null;
  }

  public BooleanExpression belongToTalent(UUID uid) {
    return talentCategory.talent.uid.eq(uid);
  }

  public BooleanExpression belongToTalentAndHaveCategory(UUID talentUid, UUID categoryUid) {
    return talentCategory.talent.uid.eq(talentUid).and(talentCategory.category.uid.eq(categoryUid));
  }
}
