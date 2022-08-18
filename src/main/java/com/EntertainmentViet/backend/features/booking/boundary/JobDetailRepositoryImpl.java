package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class JobDetailRepositoryImpl extends BaseRepositoryImpl<JobDetail, Long> implements JobDetailRepository {

  private final JobDetailPredicate jobDetailPredicate;

  public JobDetailRepositoryImpl(EntityManager em, JobDetailPredicate jobDetailPredicate) {
    super(JobDetail.class, em);
    this.jobDetailPredicate = jobDetailPredicate;
  }
}
