package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class QueryUtils {

  public <T> Query<T> createQuery() {
    return new Query<>();
  }

  public <T> Root<T> createRoot() {
    return new Root<>();
  }

  public <T> JPAQuery<T> combineJoinExpressionFrom(JPAQuery<T> query, JoinExpression<T>... joinExpressions) {
    Arrays.asList(joinExpressions).forEach(joinExpression -> joinExpression.getJoin(query));
    return query;
  }

  public static class Root<T> {
    private JPAQuery<T> base;
    private JoinExpression<T>[] joinPaths;

    public Root() {}

    public Root<T> base(JPAQuery<T> base) {
      this.base = base;
      return this;
    }

    public Root<T> joinPaths(JoinExpression... joinPaths) {
      this.joinPaths = joinPaths;
      return this;
    }

    public JPAQuery<T> build() {
      return combineJoinExpressionFrom(base, joinPaths);
    }

  }

  public static class Query<T> {
    private JPAQuery<T> root;
    private BooleanExpression[] predicates;

    public Query() {}

    public Query<T> root(JPAQuery<T> root) {
      this.root = root;
      return this;
    }

    public Query<T> predicates(BooleanExpression... predicates) {
      this.predicates = predicates;
      return this;
    }

    public List<T> getAll() {
      return build().fetch();
    }

    public Optional<T> get() {
      return Optional.ofNullable(build().fetchOne());
    }

    private JPAQuery<T> build() {
      return root.where(ExpressionUtils.allOf(predicates));
    }
  }
}
