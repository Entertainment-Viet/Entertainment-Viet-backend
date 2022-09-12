package com.EntertainmentViet.backend.features.talent.dao.packagetalent;

import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.QPackage;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PackageRepositoryImpl extends BaseRepositoryImpl<Package, Long> implements PackageRepository {

  private final QPackage packages = QPackage.package$;

  private final PackagePredicate packagePredicate;

  public PackageRepositoryImpl(EntityManager em, PackagePredicate packagePredicate) {
    super(Package.class, em);
    this.packagePredicate = packagePredicate;
  }

  @Override
  public Optional<Package> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(packages)
            .where(ExpressionUtils.allOf(
                    packagePredicate.joinAll(queryFactory),
                    packagePredicate.uidEqual(uid))
            )
            .fetchOne());
  }

  @Override
  public List<Package> findByTalentUid(UUID uid) {
    return queryFactory.selectFrom(packages)
            .where(ExpressionUtils.allOf(
                    packagePredicate.joinAll(queryFactory),
                    packagePredicate.belongToTalent(uid))
            )
            .fetch();
  }
}
