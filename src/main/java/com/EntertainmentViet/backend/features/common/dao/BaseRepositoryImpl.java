package com.EntertainmentViet.backend.features.common.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
