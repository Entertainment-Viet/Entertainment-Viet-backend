package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;

public interface LocationAddressBoundary {

	List<LocationAddressDto> findAll();

	Optional<LocationAddressDto> findByUid(UUID uid);
}
