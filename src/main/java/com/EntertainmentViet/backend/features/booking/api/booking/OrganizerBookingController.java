package com.EntertainmentViet.backend.features.booking.api.booking;

import com.EntertainmentViet.backend.exception.InconsistentDataException;
import com.EntertainmentViet.backend.features.booking.boundary.booking.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.boundary.booking.OrganizerBookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.booking.*;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = OrganizerBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class OrganizerBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/bookings";

  public static final String DONE_PATH = "/done";

  private final OrganizerBookingBoundary organizerBookingService;

  private final BookingBoundary bookingService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadBookingDto>> findByUid(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {
    boolean isOwnerUser = organizerUid.equals(RestUtils.getUidFromToken(token)) || RestUtils.isTokenContainPermissions(token,"ROOT");
    return CompletableFuture.completedFuture(bookingService.findByUid(isOwnerUser, organizerUid, uid)
        .map(bookingDto -> ResponseEntity
            .ok()
            .body(bookingDto)
        ).orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request, 
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @RequestBody @Valid CreateBookingDto createBookingDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(organizerBookingService.create(organizerUid, createBookingDto)
        .map(newBookingUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newBookingUid))
            .body(newBookingUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token,
                                                        @PathVariable("uid") UUID uid,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @RequestBody @Valid UpdateBookingDto updateBookingDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(organizerBookingService.update(organizerUid, uid, updateBookingDto)
        .map(newBookingUid -> ResponseEntity
            .ok()
            .body(newBookingUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @GetMapping
  public CompletableFuture<ResponseEntity<ListBookingResponseDto>> listBooking(JwtAuthenticationToken token,
                                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                                               @ParameterObject Pageable pageable,
                                                                               @ParameterObject ListOrganizerBookingParamDto paramDto) {
    boolean isOwnerUser = organizerUid.equals(RestUtils.getUidFromToken(token)) || RestUtils.isTokenContainPermissions(token, "ROOT");
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        organizerBookingService.listBooking(isOwnerUser, organizerUid, paramDto, pageable)
    ));
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(JwtAuthenticationToken token, 
                                                               @PathVariable("organizer_uid") UUID organizerUid, 
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (organizerBookingService.acceptBooking(organizerUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(JwtAuthenticationToken token, 
                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (organizerBookingService.rejectBooking(organizerUid, bookingUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PostMapping(value = "/{uid}" + DONE_PATH)
  public CompletableFuture<ResponseEntity<UUID>> finishBookingAndAddReview(JwtAuthenticationToken token, HttpServletRequest request,
                                                                           @PathVariable("organizer_uid") UUID organizerUid,
                                                                           @PathVariable("uid") UUID bookingUid,
                                                                           @RequestBody @Valid CreateReviewDto reviewDto) {
    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    Optional<UUID> response;
    try {
       response = organizerBookingService.finishBooingAndReview(reviewDto, organizerUid, bookingUid);
    } catch (InconsistentDataException ex) {
      log.error("Rollback database operation", ex);
      return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }

    return CompletableFuture.completedFuture(response
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.badRequest().build()));
  }
}
