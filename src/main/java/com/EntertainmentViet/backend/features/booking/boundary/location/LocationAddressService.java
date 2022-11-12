package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dao.locationaddress.LocationAddressRepository;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.ListLocationAddressResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressMapper;
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
	public ListLocationAddressResponseDto findAll(Pageable pageable) {
		var locationAddressList =
						locationAddressRepository.findAll().stream().map(locationAddressMapper::toDto).toList();
		var dataPage = RestUtils.getPageEntity(locationAddressList, pageable);
		return ListLocationAddressResponseDto.builder()
						.locationAddresses(RestUtils.toPageResponse(dataPage))
						.build();
	}

	@Override
	public Optional<LocationAddressDto> findByUid(UUID uid) {
		return Optional.ofNullable(locationAddressMapper.toDto(locationAddressRepository.findByUid(uid).orElse(null)));
	}
}
