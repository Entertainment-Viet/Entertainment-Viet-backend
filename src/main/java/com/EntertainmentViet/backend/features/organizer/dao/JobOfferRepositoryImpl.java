package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.common.utils.QueryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JobOfferRepositoryImpl extends BaseRepositoryImpl<JobOffer, Long> implements JobOfferRepository {

  private final JobOfferPredicate jobOfferPredicate;

  public JobOfferRepositoryImpl(EntityManager em, JobOfferPredicate jobOfferPredicate) {
    super(JobOffer.class, em);
    this.jobOfferPredicate = jobOfferPredicate;
  }

  @Override
  public List<JobOffer> findByOrganizerUid(UUID uid) {
    QueryUtils.Root<JobOffer> root = QueryUtils.createRoot();

    var queryRoot = root.base(jobOfferPredicate.getRootBase(queryFactory))
        .joinPaths(jobOfferPredicate.joinAll())
        .build();

    QueryUtils.Query<JobOffer> query = QueryUtils.createQuery();

    return query.root(queryRoot)
        .predicates(jobOfferPredicate.belongToOrganizer(uid))
        .getAll();
  }

  @Override
  public Optional<JobOffer> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
    QueryUtils.Root<JobOffer> root = QueryUtils.createRoot();

    var queryRoot = root.base(jobOfferPredicate.getRootBase(queryFactory))
        .joinPaths(jobOfferPredicate.joinAll())
        .build();

    QueryUtils.Query<JobOffer> query = QueryUtils.createQuery();

    return query.root(queryRoot)
        .predicates(
            jobOfferPredicate.belongToOrganizer(organizerUid),
            jobOfferPredicate.uidEqual(uid))
        .get();
  }

  @Override
  public Optional<JobOffer> findByUid(UUID uid) {
    QueryUtils.Root<JobOffer> root = QueryUtils.createRoot();

    var queryRoot = root.base(jobOfferPredicate.getRootBase(queryFactory))
        .joinPaths(jobOfferPredicate.joinAll())
        .build();

    QueryUtils.Query<JobOffer> query = QueryUtils.createQuery();
    return query.root(queryRoot)
        .predicates(jobOfferPredicate.uidEqual(uid))
        .get();
  }
}
