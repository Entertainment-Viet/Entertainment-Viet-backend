package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dao.locationaddress.LocationAddressRepository;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressMapper;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationAddressService implements LocationAddressBoundary {

	private final LocationAddressMapper locationAddressMapper;

	private final LocationAddressRepository locationAddressRepository;

	@Override
	public CustomPage<LocationAddressDto> findAll(Pageable pageable) {
		var dataPage = RestUtils.toLazyLoadPageResponse(
						locationAddressRepository.findAll(pageable)
										.map(locationAddressMapper::toDto)
		);

		if (locationAddressRepository.findAll(pageable.next()).hasContent()) {
			dataPage.getPaging().setLast(false);
		}

		return dataPage;
	}

	@Override
	public Optional<LocationAddressDto> findByUid(UUID uid) {
		return Optional.ofNullable(locationAddressMapper.toDto(locationAddressRepository.findByUid(uid).orElse(null)));
	}
}
