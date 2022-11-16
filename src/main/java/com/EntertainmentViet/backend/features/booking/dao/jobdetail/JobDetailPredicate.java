package com.EntertainmentViet.backend.features.booking.dao.jobdetail;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.BasePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;


@Component
public class JobDetailPredicate extends BasePredicate<JobDetail> {

  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;

  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(jobDetail).distinct()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type, locationType).fetchJoin()
        .fetch();

    return null;
  }
  public BooleanExpression uidEqual(Long id) {
    return jobDetail.id.eq(id);
  }
}
