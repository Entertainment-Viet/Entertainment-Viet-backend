package com.EntertainmentViet.backend.features.booking.dao.category;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl extends BaseRepositoryImpl<Category, Long> implements CategoryRepository {

  private final QCategory category = QCategory.category;

  private final CategoryPredicate categoryPredicate;

  public CategoryRepositoryImpl(EntityManager em, CategoryPredicate categoryPredicate) {
    super(Category.class, em);
    this.categoryPredicate = categoryPredicate;
  }

  @Override
  public Optional<Category> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(category)
        .where(ExpressionUtils.allOf(
            categoryPredicate.joinAll(queryFactory),
            categoryPredicate.uidEqual(uid))
        )
        .fetchOne());
  }
}
