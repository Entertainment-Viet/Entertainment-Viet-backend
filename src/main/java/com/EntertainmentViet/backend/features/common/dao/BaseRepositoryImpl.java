package com.EntertainmentViet.backend.features.common.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

  private final EntityManager em;
  protected final JPAQueryFactory queryFactory;

  public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
    super(domainClass, em);
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  protected OrderSpecifier<?>[] getSortedColumn(Sort sorts, Class entityClass) {
    PathBuilder<JobOffer> entityPath = new PathBuilder<>(entityClass, entityClass.getSimpleName());
    return sorts.stream()
            .map(order -> new OrderSpecifier(Order.valueOf(order.getDirection().name()), entityPath.get(order.getProperty())))
            .toArray(OrderSpecifier[]::new);
  }
}
