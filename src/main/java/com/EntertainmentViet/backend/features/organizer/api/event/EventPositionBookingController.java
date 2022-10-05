package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.event.EventPositionBookingBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreatePositionApplicantDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = EventPositionBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class EventPositionBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events/{event_uid}/positions/{position_id}/bookings";

  private final EventPositionBookingBoundary eventPositionBookingService;

  @GetMapping
  public CompletableFuture<ResponseEntity<Page<ReadBookingDto>>> listApplicant(JwtAuthenticationToken token,
                                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                                               @PathVariable("event_uid") UUID eventUid,
                                                                               @PathVariable("position_id") UUID positionUid,
                                                                               @ParameterObject Pageable pageable,
                                                                               @ParameterObject ListOrganizerBookingParamDto paramDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to read information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        eventPositionBookingService.findByPositionUid(organizerUid, eventUid, positionUid, paramDto, pageable)
    ));
  }

  @PostMapping
  public CompletableFuture<ResponseEntity<UUID>> applyToPosition(JwtAuthenticationToken token, HttpServletRequest request,
                                                                 @PathVariable("organizer_uid") UUID organizerUid,
                                                                 @PathVariable("event_uid") UUID eventUid,
                                                                 @PathVariable("position_id") UUID positionUid,
                                                                 @RequestBody @Valid CreatePositionApplicantDto createPositionApplicantDto) {
    return CompletableFuture.completedFuture(eventPositionBookingService.create(organizerUid, eventUid, positionUid, createPositionApplicantDto)
        .map(newBookingUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newBookingUid))
            .body(newBookingUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(JwtAuthenticationToken token,
                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                               @PathVariable("event_uid") UUID eventUid,
                                                               @PathVariable("position_id") UUID positionUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (eventPositionBookingService.acceptBooking(organizerUid, eventUid, positionUid, bookingUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(JwtAuthenticationToken token,
                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                               @PathVariable("event_uid") UUID eventUid,
                                                               @PathVariable("position_id") UUID positionUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (eventPositionBookingService.rejectBooking(organizerUid, eventUid, positionUid, bookingUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
