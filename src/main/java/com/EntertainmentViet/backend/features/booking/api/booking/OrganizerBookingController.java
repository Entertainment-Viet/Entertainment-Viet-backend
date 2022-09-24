package com.EntertainmentViet.backend.features.booking.api.booking;

import com.EntertainmentViet.backend.features.booking.boundary.booking.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.boundary.booking.OrganizerBookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = OrganizerBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class OrganizerBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/bookings";

  private final OrganizerBookingBoundary organizerBookingService;

  private final BookingBoundary bookingService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadBookingDto>> findByUid(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    return CompletableFuture.completedFuture(bookingService.findByUid(organizerUid, uid)
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

    return  CompletableFuture.completedFuture(bookingService.createForOrganizer(organizerUid, createBookingDto)
        .map(newBookingDto -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newBookingDto))
            .body(newBookingDto)
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

    return  CompletableFuture.completedFuture(bookingService.update(organizerUid, uid, updateBookingDto)
        .map(newBookingDto -> ResponseEntity
            .ok()
            .body(newBookingDto)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }
  @GetMapping()
  public CompletableFuture<ResponseEntity<Page<ReadBookingDto>>> listBooking(JwtAuthenticationToken token,
                                                                             @PathVariable("organizer_uid") UUID organizerUid,
                                                                             @ParameterObject Pageable pageable,
                                                                             @ParameterObject ListOrganizerBookingParamDto paramDto) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
            organizerBookingService.listBooking(organizerUid, paramDto, pageable)
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

  @PutMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(JwtAuthenticationToken token, 
                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (organizerBookingService.rejectBooking(organizerUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
