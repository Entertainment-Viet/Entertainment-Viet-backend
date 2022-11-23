package com.EntertainmentViet.backend.features.organizer.dao.joboffer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ListJobOfferParamDto;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class JobOfferRepositoryImpl extends BaseRepositoryImpl<JobOffer, Long> implements JobOfferRepository {

  private final QJobOffer jobOffer = QJobOffer.jobOffer;

  private final JobOfferPredicate jobOfferPredicate;

  public JobOfferRepositoryImpl(EntityManager em, JobOfferPredicate jobOfferPredicate) {
    super(JobOffer.class, em);
    this.jobOfferPredicate = jobOfferPredicate;
  }

  @Override
  public List<JobOffer> findByOrganizerUid(UUID uid, ListJobOfferParamDto paramDto, Pageable pageable) {
    return queryFactory.selectFrom(jobOffer)
        .where(ExpressionUtils.allOf(
            jobOfferPredicate.joinAll(queryFactory),
            jobOfferPredicate.belongToOrganizer(uid),
            jobOfferPredicate.fromParams(paramDto),
            jobOfferPredicate.isArchived().not()
        ))
        .orderBy(getSortedColumn(pageable.getSort(), JobOffer.class))
        .fetch();
  }

  @Override
  public Optional<JobOffer> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(jobOffer)
        .where(ExpressionUtils.allOf(
            jobOfferPredicate.joinAll(queryFactory),
            jobOfferPredicate.belongToOrganizer(organizerUid),
            jobOfferPredicate.uidEqual(uid)),
            jobOfferPredicate.isArchived().not()
        )
        .fetchOne());
  }

  @Override
  public Optional<JobOffer> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(jobOffer)
        .where(ExpressionUtils.allOf(
            jobOfferPredicate.joinAll(queryFactory),
            jobOfferPredicate.uidEqual(uid)),
            jobOfferPredicate.isArchived().not()
        )
        .fetchOne());
  }
}
