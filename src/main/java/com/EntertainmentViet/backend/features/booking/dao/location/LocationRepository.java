package com.EntertainmentViet.backend.features.booking.dao.location;

import javax.transaction.Transactional;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.features.booking.dto.location.ListLocationParamDto;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface LocationRepository extends IdentifiableRepository<Location> {

	Page<Location> findAll(ListLocationParamDto paramDto, Pageable pageable);
}

