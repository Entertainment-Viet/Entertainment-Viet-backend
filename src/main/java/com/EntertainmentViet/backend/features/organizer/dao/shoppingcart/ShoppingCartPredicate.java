package com.EntertainmentViet.backend.features.organizer.dao.shoppingcart;

import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ListCartItemParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

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


  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(organizerShoppingCart).distinct()
        .leftJoin(organizerShoppingCart.organizer, organizer).fetchJoin()
        .leftJoin(organizerShoppingCart.talentPackage, aPackage).fetchJoin()
        .leftJoin(aPackage.talent, talent).fetchJoin()
        .leftJoin(aPackage.jobDetail, jobDetail).fetchJoin()
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