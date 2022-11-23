package com.EntertainmentViet.backend.features.booking.dao.location;

import com.EntertainmentViet.backend.domain.values.LocationType;
import com.EntertainmentViet.backend.features.common.dao.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class LocationTypeRepositoryImpl extends BaseRepositoryImpl<LocationType, Long> implements LocationTypeRepository {

  protected LocationTypeRepositoryImpl(EntityManager em) {
    super(LocationType.class, em);
  }
}
