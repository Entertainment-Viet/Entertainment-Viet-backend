package com.EntertainmentViet.backend.features.booking.dao.location;

import javax.transaction.Transactional;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface LocationRepository extends IdentifiableRepository<Location> {

}

