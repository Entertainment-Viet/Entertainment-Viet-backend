package com.EntertainmentViet.backend.features.common.dao;

import javax.persistence.EntityManager;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

  private final EntityManager em;
  protected final JPAQueryFactory queryFactory;

  protected BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  protected <T> OrderSpecifier<?>[] getSortedColumn(Sort sorts, Class<T> entityClass) {
    PathBuilder<T> entityPath = new PathBuilder<>(entityClass, StringUtils.uncapitalize(entityClass.getSimpleName()));
    return sorts.stream()
            .map(order -> new OrderSpecifier(Order.valueOf(order.getDirection().name()), entityPath.get(order.getProperty())))
            .toArray(OrderSpecifier[]::new);
  }
}
