package com.EntertainmentViet.backend.features.talent.dao.packagetalent;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
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
  private final QCategory parentCategory = new QCategory("parent");

  private final QOrganizer organizer = QOrganizer.organizer;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {

    // join bookings
    var packages = queryFactory.selectFrom(talentPackage).distinct()
        .leftJoin(talentPackage.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .leftJoin(category.parent, parentCategory).fetchJoin()
          .leftJoin(talentPackage.talent, talent).fetchJoin()
          .fetch();

    queryFactory.selectFrom(talentPackage).distinct()
        .leftJoin(talentPackage.orders, booking).fetchJoin()
        .leftJoin(booking.talent, talent).fetchJoin()
        .leftJoin(booking.organizer, organizer).fetchJoin()
        .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
        .leftJoin(jobDetail.category, category).fetchJoin()
        .where(talentPackage.in(packages))
        .fetch();

    return null;
  }

  public BooleanExpression belongToTalent(UUID uid) {
    return talentPackage.talent.uid.eq(uid);
  }

  public Predicate fromParams(ListPackageParamDto paramDto) {
    var predicate = defaultPredicate();

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

    return predicate;
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talentPackage.uid.eq(uid);
  }
}
