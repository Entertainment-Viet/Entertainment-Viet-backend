package com.EntertainmentViet.backend.features.organizer.dao.joboffer;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocationAddress;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ListJobOfferParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobOfferPredicate extends IdentifiablePredicate<JobOffer> {

  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;

  private final QOrganizer organizer = QOrganizer.organizer;

  private final QLocationAddress locationAddress = QLocationAddress.locationAddress;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(jobOffer).distinct()
        .leftJoin(jobOffer.organizer, organizer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(jobDetail.location, locationAddress).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromParams(ListJobOfferParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto.getName() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              jobOffer.name.like("%"+paramDto.getName()+"%")
      );
    }
    if (paramDto.getIsActive() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              jobOffer.isActive.eq(paramDto.getIsActive())
      );
    }
    if (paramDto.getOrganizer() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              jobOffer.organizer.displayName.like("%"+paramDto.getOrganizer()+"%")
      );
    }
    if (paramDto.getCategory() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              jobOffer.jobDetail.category.uid.eq(paramDto.getCategory())
      );
    }
    return predicate;
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
      return jobOffer.organizer.uid.eq(uid);
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return jobOffer.uid.eq(uid);
  }
}
