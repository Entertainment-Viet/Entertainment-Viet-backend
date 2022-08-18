package com.EntertainmentViet.backend.features.common.dao;

import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class BasePredicate<T> {

  public abstract JoinExpression joinAll();

  public abstract JPAQuery<T> getRootBase(JPAQueryFactory queryFactory);
}
