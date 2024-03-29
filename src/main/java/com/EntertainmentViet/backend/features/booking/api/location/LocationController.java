package com.EntertainmentViet.backend.features.booking.api.location;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.EntertainmentViet.backend.features.booking.boundary.location.LocationBoundary;
import com.EntertainmentViet.backend.features.booking.dto.location.ListLocationParamDto;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = LocationController.REQUEST_MAPPING_PATH)
public class LocationController {

	public static final String REQUEST_MAPPING_PATH = "/locations";

	private final LocationBoundary locationService;

	@GetMapping
	public CompletableFuture<ResponseEntity<CustomPage<LocationDto>>> findAll(
					@ParameterObject ListLocationParamDto paramDto,
					@ParameterObject Pageable pageable
	) {
		return CompletableFuture.completedFuture(ResponseEntity.ok().body(
						locationService.findAll(paramDto, pageable))
		);
	}

	@GetMapping(value = "/{uid}")
	public CompletableFuture<ResponseEntity<LocationDto>> findById(@PathVariable("uid") UUID uid) {
		return CompletableFuture.completedFuture(locationService.findByUid(uid)
						.map(locationDto -> ResponseEntity
										.ok()
										.body(locationDto)
						)
						.orElse(ResponseEntity.notFound().build())
		);
	}

}
