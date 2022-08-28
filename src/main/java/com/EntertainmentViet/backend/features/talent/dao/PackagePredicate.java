package com.EntertainmentViet.backend.features.talent.dao;

import com.EntertainmentViet.backend.domain.entities.booking.QBooking;
import com.EntertainmentViet.backend.domain.entities.booking.QJobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.QOrganizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.domain.entities.talent.QTalent;
import com.EntertainmentViet.backend.domain.values.QCategory;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
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

  private final QOrganizer organizer = QOrganizer.organizer;

  @Override
  public Predicate joinAll(JPAQueryFactory queryFactory) {

    // join bookings
    queryFactory.selectFrom(talentPackage).distinct()
          .leftJoin(talentPackage.talent, talent).fetchJoin()
          .leftJoin(talentPackage.orders, booking).fetchJoin()
          .leftJoin(booking.organizer, organizer).fetchJoin()
          .leftJoin(booking.jobDetail, jobDetail).fetchJoin()
          .leftJoin(talentPackage.jobDetail, jobDetail).fetchJoin()
          .leftJoin(jobDetail.category, category).fetchJoin()
          .fetch();

    return null;
  }

  public BooleanExpression belongToTalent(UUID uid) {
    return talentPackage.talent.uid.eq(uid);
  }

  @Override
  public BooleanExpression uidEqual(UUID uid) {
    return talentPackage.uid.eq(uid);
  }
}
