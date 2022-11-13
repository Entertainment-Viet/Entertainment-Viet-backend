package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import org.springframework.data.domain.Pageable;

public interface LocationAddressBoundary {

	CustomPage<LocationAddressDto> findAll(Pageable pageable);

	Optional<LocationAddressDto> findByUid(UUID uid);
}
