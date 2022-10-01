package com.EntertainmentViet.backend.features.booking.dao.category;

import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryPredicate extends IdentifiablePredicate<Category> {

  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parent");

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(category).distinct()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .fetch();

    return null;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return category.uid.eq(uid);
  }
}
