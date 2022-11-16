package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.location.LocationDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import org.springframework.data.domain.Pageable;

public interface LocationBoundary {

	CustomPage<LocationDto> findAll(Pageable pageable);

	Optional<LocationDto> findByUid(UUID uid);
}
