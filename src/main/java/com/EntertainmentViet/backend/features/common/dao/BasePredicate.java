package com.EntertainmentViet.backend.features.common.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class BasePredicate<T> {

  public abstract Predicate joinAll(JPAQueryFactory queryFactory);
}
