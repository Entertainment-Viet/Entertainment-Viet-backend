package com.EntertainmentViet.backend.features.common;

import com.querydsl.jpa.impl.JPAQuery;

@FunctionalInterface
public interface JoinExpression<T> {
  JPAQuery<T> getJoin(JPAQuery<T> query);
}
