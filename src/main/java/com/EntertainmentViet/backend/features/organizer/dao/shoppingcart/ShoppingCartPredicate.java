package com.EntertainmentViet.backend.features.organizer.dao.shoppingcart;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ListCartItemParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShoppingCartPredicate extends IdentifiablePredicate<OrganizerShoppingCart> {

  private final QOrganizerShoppingCart organizerShoppingCart = QOrganizerShoppingCart.organizerShoppingCart;

  private final QOrganizer organizer = QOrganizer.organizer;
  private final QPackage aPackage = QPackage.package$;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QTalent talent = QTalent.talent;
  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parent");
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parent");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(organizerShoppingCart).distinct()
        .leftJoin(organizerShoppingCart.organizer, organizer).fetchJoin()
        .leftJoin(organizerShoppingCart.talentPackage, aPackage).fetchJoin()
        .leftJoin(aPackage.talent, talent).fetchJoin()
        .leftJoin(aPackage.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromParams(ListCartItemParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto.getWorkType() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          organizerShoppingCart.talentPackage.jobDetail.workType.eq(WorkType.ofI18nKey(paramDto.getWorkType()))
      );
    }
    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          organizerShoppingCart.talentPackage.jobDetail.category.uid.eq(paramDto.getCategory())
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return organizerShoppingCart.uid.eq(uid);
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
    return organizerShoppingCart.organizer.uid.eq(uid);
  }

}
