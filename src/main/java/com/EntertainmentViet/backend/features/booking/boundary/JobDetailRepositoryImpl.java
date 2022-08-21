package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class JobDetailRepositoryImpl extends BaseRepositoryImpl<JobDetail, Long> implements JobDetailRepository {

  private final JobDetailPredicate jobDetailPredicate;

  public JobDetailRepositoryImpl(EntityManager em, JobDetailPredicate jobDetailPredicate) {
    super(JobDetail.class, em);
    this.jobDetailPredicate = jobDetailPredicate;
  }

  @Override
  public Optional<JobDetail> findById(Long id) {
    QueryUtils.Root<JobDetail> root = QueryUtils.createRoot();

    var queryRoot = root.base(jobDetailPredicate.getRootBase(queryFactory))
            .joinPaths(jobDetailPredicate.joinAll())
            .build();

    QueryUtils.Query<JobDetail> query = QueryUtils.createQuery();
    return query.root(queryRoot)
            .predicates(jobDetailPredicate.uidEqual(id))
            .get();
  }
}
