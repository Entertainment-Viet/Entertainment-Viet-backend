package com.EntertainmentViet.backend.features.booking.dao.location;

import com.EntertainmentViet.backend.domain.values.LocationType;
import com.EntertainmentViet.backend.features.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface LocationTypeRepository extends BaseRepository<LocationType, Long> {
}

