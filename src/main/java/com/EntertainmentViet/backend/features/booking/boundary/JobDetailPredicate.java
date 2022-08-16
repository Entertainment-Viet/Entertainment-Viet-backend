package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.EntertainmentViet.backend.features.common.dao.BasePredicate;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

@Component
public class JobDetailPredicate extends BasePredicate<JobDetail> {

  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;

  @Override
  public JPAQuery<JobDetail> getRootBase(JPAQueryFactory queryFactory) {
    return queryFactory.selectFrom(jobDetail);
  }

  @Override
  public JoinExpression joinAll() {
    return query -> QueryUtils.combineJoinExpressionFrom(query, joinCategory());
  }

  public JoinExpression joinCategory() {
    return query -> query.leftJoin(jobDetail.category, category).fetchJoin();
  }
}
