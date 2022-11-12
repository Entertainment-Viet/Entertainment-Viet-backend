package com.EntertainmentViet.backend.features.booking.api.location;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.EntertainmentViet.backend.features.booking.boundary.location.LocationAddressBoundary;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.ListLocationAddressResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
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
@RequestMapping(path = LocationAddressController.REQUEST_MAPPING_PATH)
public class LocationAddressController {

	public static final String REQUEST_MAPPING_PATH = "/locations";

	private final LocationAddressBoundary locationService;

	@GetMapping
	public CompletableFuture<ResponseEntity<ListLocationAddressResponseDto>> findAll(
					@ParameterObject Pageable pageable
	) {
		return CompletableFuture.completedFuture(ResponseEntity.ok().body(
						locationService.findAll(pageable))
		);
	}

	@GetMapping(value = "/{uid}")
	public CompletableFuture<ResponseEntity<LocationAddressDto>> findAll(@PathVariable("uid") UUID uid) {
		return CompletableFuture.completedFuture(locationService.findByUid(uid)
						.map(categoryDto -> ResponseEntity
										.ok()
										.body(categoryDto)
						)
						.orElse(ResponseEntity.notFound().build())
		);
	}

}
