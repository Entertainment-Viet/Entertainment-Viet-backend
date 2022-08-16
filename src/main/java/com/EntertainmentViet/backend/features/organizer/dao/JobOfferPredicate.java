package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.features.booking.boundary.JobDetailPredicate;
import com.EntertainmentViet.backend.features.common.JoinExpression;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobOfferPredicate extends IdentifiablePredicate<JobOffer> {

  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;

  private final JobDetailPredicate jobDetailPredicate;

  public JoinExpression joinJobDetail() {
    return query -> query.leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin();
  }

  @Override
  public JPAQuery<JobOffer> getRootBase(JPAQueryFactory queryFactory) {
    return queryFactory.selectFrom(jobOffer);
  }

  @Override
  public JoinExpression joinAll() {
    return query -> QueryUtils.combineJoinExpressionFrom(query,
        joinJobDetail(),
        jobDetailPredicate.joinAll()
    );
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
      return jobOffer.organizer.uid.eq(uid);
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return jobOffer.uid.eq(uid);
  }
}
