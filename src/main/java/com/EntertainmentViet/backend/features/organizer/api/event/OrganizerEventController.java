package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.QueryParamsUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.event.EventBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.UpdateEventDto;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = OrganizerEventController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class OrganizerEventController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events";

  public static final String BOOKING_PATH = "/bookings";

  private final EventBoundary eventService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadEventDto>>> findByOrganizerUid(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid,
                                                                                        @ParameterObject Pageable pageable,
                                                                                        @ParameterObject ListEventParamDto paramDto) {
    if (QueryParamsUtils.isInvalidParams(paramDto)) {
      log.warn(String.format("Currency is not provided '%s'", paramDto.getCurrency()));
      return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(RestUtils.toPageResponse(
        eventService.findByOrganizerUid(organizerUid, paramDto, pageable)
    )));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @RequestBody @Valid CreateEventDto createEventDto) {
    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(eventService.create(createEventDto, organizerUid)
        .map(newEventUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newEventUid))
            .body(newEventUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadEventDto>> findByUid(JwtAuthenticationToken token,
                                                                         @PathVariable("organizer_uid") UUID organizerUid,
                                                                         @PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(eventService.findByOrganizerUidAndUid(organizerUid, uid)
        .map( eventDto -> ResponseEntity
            .ok()
            .body(eventDto)
        )
        .orElse(ResponseEntity.notFound().build())
    );
  }

  @PutMapping(value = "/{uid}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @RequestBody @Valid UpdateEventDto updateEventDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(eventService.update(updateEventDto, organizerUid, uid)
        .map(newEventUid -> ResponseEntity
            .ok()
            .body(newEventUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> delete(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (eventService.delete(uid, organizerUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @GetMapping(value = "/{uid}" + BOOKING_PATH)
  public CompletableFuture<ResponseEntity<ListBookingResponseDto>> listBooking(JwtAuthenticationToken token,
                                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                                               @PathVariable("uid") UUID uid,
                                                                               @ParameterObject Pageable pageable,
                                                                               @ParameterObject ListOrganizerBookingParamDto paramDto) {
    boolean isOwnerUser = organizerUid.equals(RestUtils.getUidFromToken(token)) || RestUtils.isTokenContainPermissions(token, "ROOT");
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        eventService.listBooking(isOwnerUser, organizerUid, paramDto, pageable)
    ));
  }
}
