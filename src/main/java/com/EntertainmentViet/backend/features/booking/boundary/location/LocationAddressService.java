package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.EntertainmentViet.backend.features.booking.dao.locationaddress.LocationAddressRepository;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationAddressService implements LocationAddressBoundary {

	private final LocationAddressMapper locationAddressMapper;

	private final LocationAddressRepository locationAddressRepository;

	@Override
	public List<LocationAddressDto> findAll() {
		return locationAddressRepository.findAll().stream().map(locationAddressMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public Optional<LocationAddressDto> findByUid(UUID uid) {
		return Optional.ofNullable(locationAddressMapper.toDto(locationAddressRepository.findByUid(uid).orElse(null)));
	}
}
