package com.EntertainmentViet.backend.features.booking.dao;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class JobDetailRepositoryImpl extends BaseRepositoryImpl<JobDetail, Long> implements JobDetailRepository {

  private final QJobDetail jobDetail = QJobDetail.jobDetail;

  private final JobDetailPredicate jobDetailPredicate;

  public JobDetailRepositoryImpl(EntityManager em, JobDetailPredicate jobDetailPredicate) {
    super(JobDetail.class, em);
    this.jobDetailPredicate = jobDetailPredicate;
  }

  @Override
  public Optional<JobDetail> findById(Long uid) {
    return Optional.ofNullable(queryFactory.selectFrom(jobDetail)
        .where(ExpressionUtils.allOf(
            jobDetailPredicate.joinAll(queryFactory),
            jobDetailPredicate.uidEqual(uid))
        )
        .fetchOne());
  }
}
