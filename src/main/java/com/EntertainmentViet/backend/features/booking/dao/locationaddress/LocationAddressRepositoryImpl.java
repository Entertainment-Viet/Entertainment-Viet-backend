package com.EntertainmentViet.backend.features.booking.dao.locationaddress;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;

import com.EntertainmentViet.backend.domain.values.LocationAddress;
import com.EntertainmentViet.backend.domain.values.QLocationAddress;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import com.querydsl.core.types.ExpressionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class LocationAddressRepositoryImpl extends BaseRepositoryImpl<LocationAddress, Long>
        implements LocationAddressRepository {

  private final QLocationAddress locationAddress = QLocationAddress.locationAddress;

  private final LocationAddressPredicate locationAddressPredicate;

  public LocationAddressRepositoryImpl(EntityManager em, LocationAddressPredicate locationAddressPredicate) {
    super(LocationAddress.class, em);
    this.locationAddressPredicate = locationAddressPredicate;
  }

  @Override
  public Optional<LocationAddress> findByUid(UUID uid) {
    return Optional.ofNullable(queryFactory.selectFrom(locationAddress)
            .where(ExpressionUtils.allOf(
                    locationAddressPredicate.joinAll(queryFactory),
                    locationAddressPredicate.uidEqual(uid))
            )
            .fetchOne());
  }
}
