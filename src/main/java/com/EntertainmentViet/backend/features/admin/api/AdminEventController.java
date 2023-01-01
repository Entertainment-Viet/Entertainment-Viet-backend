package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.features.admin.boundary.bookings.AdminBookingBoundary;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListEventBookingParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = AdminEventController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminEventController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/events";
  public static final String BOOKING_PATH = "/bookings";

  private final AdminBookingBoundary adminBookingService;

  @GetMapping(value = "/{event_uid}" + BOOKING_PATH)
  public CompletableFuture<ResponseEntity<AdminListBookingResponseDto>> listBooking(@PathVariable("event_uid") UUID eventUid,
                                                                                    @ParameterObject Pageable pageable,
                                                                                    @ParameterObject ListEventBookingParamDto paramDto) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        adminBookingService.listEventBooking(eventUid, paramDto, pageable)
    ));
  }
}
