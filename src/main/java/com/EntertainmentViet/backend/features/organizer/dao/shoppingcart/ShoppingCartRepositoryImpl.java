package com.EntertainmentViet.backend.features.organizer.dao.shoppingcart;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizerShoppingCart;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ListCartItemParamDto;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ShoppingCartRepositoryImpl extends BaseRepositoryImpl<OrganizerShoppingCart, Long> implements ShoppingCartRepository {

  private final QOrganizerShoppingCart organizerShoppingCart = QOrganizerShoppingCart.organizerShoppingCart;

  private final ShoppingCartPredicate shoppingCartPredicate;

  public ShoppingCartRepositoryImpl(EntityManager em, ShoppingCartPredicate shoppingCartPredicate) {
    super(OrganizerShoppingCart.class, em);
    this.shoppingCartPredicate = shoppingCartPredicate;
  }

  @Override
  public Optional<OrganizerShoppingCart> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(organizerShoppingCart)
        .where(ExpressionUtils.allOf(
            shoppingCartPredicate.joinAll(queryFactory),
            shoppingCartPredicate.uidEqual(uid))
        )
        .fetchOne());
  }

  @Override
  public List<OrganizerShoppingCart> findByOrganizerUid(UUID uid, ListCartItemParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(organizerShoppingCart)
        .where(ExpressionUtils.allOf(
            shoppingCartPredicate.joinAll(queryFactory),
            shoppingCartPredicate.belongToOrganizer(uid),
        shoppingCartPredicate.fromParams(paramDto)
        ))
        .orderBy(getSortedColumn(pageable.getSort(), JobOffer.class))
        .fetch();
  }
}
