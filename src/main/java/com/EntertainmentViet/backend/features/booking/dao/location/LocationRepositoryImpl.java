package com.EntertainmentViet.backend.features.booking.dao.location;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepositoryImpl extends BaseRepositoryImpl<Location, Long>
        implements LocationRepository {

  private final QLocation location = QLocation.location;

  private final LocationPredicate locationPredicate;

  public LocationRepositoryImpl(EntityManager em, LocationPredicate locationPredicate) {
    super(Location.class, em);
    this.locationPredicate = locationPredicate;
  }

  @Override
  public Optional<Location> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(location)
            .where(ExpressionUtils.allOf(
                    locationPredicate.joinAll(queryFactory),
                    locationPredicate.uidEqual(uid))
            )
            .fetchOne());
  }
}
