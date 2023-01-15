package com.EntertainmentViet.backend.features.talent.dao.packagetalent;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PackagePredicate extends IdentifiablePredicate<Package> {

  private final QPackage talentPackage = QPackage.package$;
  private final QJobDetail jobDetail = QJobDetail.jobDetail;
  private final QBooking booking = QBooking.booking;

  private final QTalent talent = QTalent.talent;
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

    // join bookings
    var packages = queryFactory.selectFrom(talentPackage).distinct()
        .leftJoin(talentPackage.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(talentPackage.talent, talent).fetchJoin()
        .fetch();

    queryFactory.selectFrom(talentPackage).distinct()
        .leftJoin(talentPackage.orders, booking).fetchJoin()
        .leftJoin(booking.talent, talent).fetchJoin()
        .leftJoin(booking.organizer, organizer).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
        .leftJoin(jobDetail.location, location).fetchJoin()
        .leftJoin(location.type(), locationType).fetchJoin()
        .leftJoin(location.parent(), parentLocation).fetchJoin()
        .leftJoin(parentLocation.type(), parentLocationType).fetchJoin()
        .leftJoin(parentLocation.parent(), grandparentLocation).fetchJoin()
        .leftJoin(grandparentLocation.type(), grandParentLocationType).fetchJoin()
        .where(talentPackage.in(packages))
        .fetch();

    return null;
  }

  public BooleanExpression belongToTalent(UUID uid) {
    return talentPackage.talent.uid.eq(uid);
  }

  public Predicate fromParams(ListPackageParamDto paramDto) {
    var predicate = defaultPredicate();

    if (paramDto == null) {
      return predicate;
    }

    if (paramDto.getName() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          talentPackage.name.like("%"+paramDto.getName()+"%")
      );
    }
    if (paramDto.getIsActive() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getIsActive() ? talentPackage.isActive.isTrue() : talentPackage.isActive.isFalse()
      );
    }
    if (paramDto.getTalent() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          talentPackage.talent.displayName.like("%"+paramDto.getTalent()+"%")
      );
    }
    if (paramDto.getOrderCount() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          talentPackage.orders.size().eq(paramDto.getOrderCount())
      );
    }
    if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() == null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talentPackage.jobDetail.price.currency.eq(Currency.ofI18nKey(paramDto.getCurrency())),
              talentPackage.jobDetail.price.min.loe(paramDto.getMaxPrice())
      );
    }
    else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() == null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talentPackage.jobDetail.price.currency.eq(Currency.ofI18nKey(paramDto.getCurrency())),
              talentPackage.jobDetail.price.max.goe(paramDto.getMinPrice())
      );
    }
    else if (paramDto.getCurrency() != null && paramDto.getMaxPrice() != null && paramDto.getMinPrice() != null) {
      predicate = ExpressionUtils.allOf(
              predicate,
              talentPackage.jobDetail.price.currency.eq(Currency.ofI18nKey(paramDto.getCurrency())),
          Expressions.asDateTime(paramDto.getMinPrice()).between(talentPackage.jobDetail.price.min, talentPackage.jobDetail.price.max)
                  .or(Expressions.asDateTime(paramDto.getMaxPrice()).between(talentPackage.jobDetail.price.min, talentPackage.jobDetail.price.max))
              .or(talentPackage.jobDetail.price.min.between(Expressions.asDateTime(paramDto.getMinPrice()), Expressions.asDateTime(paramDto.getMaxPrice()))
                  .or(talentPackage.jobDetail.price.max.between(Expressions.asDateTime(paramDto.getMinPrice()), Expressions.asDateTime(paramDto.getMaxPrice())))
              )
      );
    }

    if (paramDto.getLocationId() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          Expressions.booleanTemplate("check_location({0}, {1}) > 0", paramDto.getLocationId(), talentPackage.jobDetail.location.uid)
      );
    }

    if (paramDto.getWithArchived() != null) {
      predicate = ExpressionUtils.allOf(
          predicate,
          paramDto.getWithArchived() ? isArchived(true).or(isTalentArchived(true)) :
              isArchived(false).and(isTalentArchived(false))
      );
    }
    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talentPackage.uid.eq(uid);
  }

  public BooleanExpression isArchived(boolean archived) {
    return talentPackage.archived.eq(archived);
  }

      public BooleanExpression isTalentArchived(boolean archived) {
        return talentPackage.talent.archived.eq(archived);
      }
}
