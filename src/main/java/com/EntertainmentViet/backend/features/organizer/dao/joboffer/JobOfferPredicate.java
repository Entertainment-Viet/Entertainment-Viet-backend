package com.EntertainmentViet.backend.features.organizer.dao.joboffer;

import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QJobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ListJobOfferParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobOfferPredicate extends IdentifiablePredicate<JobOffer> {

  private final QJobOffer jobOffer = QJobOffer.jobOffer;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QCategory category = QCategory.category;
  private final QCategory parentCategory = new QCategory("parentCategory");
  private final QOrganizer organizer = QOrganizer.organizer;
  private final QLocation location = QLocation.location;
  private final QLocationType locationType = QLocationType.locationType;
  private final QLocation parentLocation = new QLocation("parentLocation");
  private final QLocationType parentLocationType = new QLocationType("parentLocationType");
  private final QLocation grandparentLocation = new QLocation("grandparentLocation");
  private final QLocationType grandParentLocationType = new QLocationType("grandParentLocationType");
  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {
    queryFactory.selectFrom(jobOffer).distinct()
        .leftJoin(jobOffer.organizer, organizer).fetchJoin()
        .leftJoin(jobOffer.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .fetch();

    return null;
  }

  public Predicate fromParams(ListJobOfferParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto == null) {
      return predicate;
    }

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

    if (paramDto.getWithArchived() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getWithArchived() ? isArchived(true).or(isOrganizerArchived(true)) :
              isArchived(false).and(isOrganizerArchived(false))
      );
    }

    return predicate;
  }

  public BooleanExpression belongToOrganizer(UUID uid) {
      return jobOffer.organizer.uid.eq(uid);
  }

  public BooleanExpression isArchived(boolean archived) {
    return jobOffer.archived.eq(archived);
  }

  public BooleanExpression isOrganizerArchived(boolean archived) {
        return jobOffer.organizer.archived.eq(archived);
      }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return jobOffer.uid.eq(uid);
  }
}
