package com.EntertainmentViet.backend.features.organizer.dao.joboffer;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ListJobOfferParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            jobOfferPredicate.fromParams(paramDto)
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
            jobOfferPredicate.uidEqual(uid))
        )
        .fetchOne());
  }

  @Override
  public Optional<JobOffer> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(jobOffer)
        .where(ExpressionUtils.allOf(
            jobOfferPredicate.joinAll(queryFactory),
            jobOfferPredicate.uidEqual(uid))
        )
        .fetchOne());
  }
}
