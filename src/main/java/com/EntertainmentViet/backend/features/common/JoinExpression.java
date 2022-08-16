package com.EntertainmentViet.backend.features.common;

import com.querydsl.jpa.impl.JPAQuery;

@FunctionalInterface
public interface JoinExpression {
  JPAQuery<?> getJoin(JPAQuery<?> query);
}
