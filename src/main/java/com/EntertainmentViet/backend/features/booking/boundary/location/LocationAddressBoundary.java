package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.locationaddress.ListLocationAddressResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
import org.springframework.data.domain.Pageable;

public interface LocationAddressBoundary {

	ListLocationAddressResponseDto findAll(Pageable pageable);

	Optional<LocationAddressDto> findByUid(UUID uid);
}
