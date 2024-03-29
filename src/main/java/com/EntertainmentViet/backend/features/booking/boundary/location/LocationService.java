package com.EntertainmentViet.backend.features.booking.boundary.location;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dao.location.LocationRepository;
import com.EntertainmentViet.backend.features.booking.dto.location.ListLocationParamDto;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationDto;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService implements LocationBoundary {

	private final LocationMapper locationMapper;

	private final LocationRepository locationRepository;

	@Override
	public CustomPage<LocationDto> findAll(ListLocationParamDto paramDto, Pageable pageable) {
		var dataPage = RestUtils.toLazyLoadPageResponse(
						locationRepository.findAll(paramDto, pageable)
										.map(locationMapper::toDto)
		);

		if (locationRepository.findAll(paramDto, pageable.next()).hasContent()) {
			dataPage.getPaging().setLast(false);
		}

		return dataPage;
	}

	@Override
	public Optional<LocationDto> findByUid(UUID uid) {
		return Optional.ofNullable(locationMapper.toDto(locationRepository.findByUid(uid).orElse(null)));
	}
}
