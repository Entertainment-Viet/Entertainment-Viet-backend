package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.event.EventOpenPositionBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventOpenPositionDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventPositionParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventOpenPositionDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.UpdateEventOpenPositionDto;
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
@RequestMapping(path = EventPositionController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class EventPositionController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events/{event_uid}/positions";

  private final EventOpenPositionBoundary eventPositionService;

  @GetMapping
  public CompletableFuture<ResponseEntity<Page<ReadEventOpenPositionDto>>> findByEventUid(JwtAuthenticationToken token,
                                                                                          @PathVariable("organizer_uid") UUID organizerUid,
                                                                                          @PathVariable("event_uid") UUID eventUid,
                                                                                          @ParameterObject Pageable pageable,
                                                                                          @ParameterObject ListEventPositionParamDto paramDto) {

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        eventPositionService.findByOrganizerUidAndEventUid(organizerUid, eventUid, paramDto, pageable)
    ));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @PathVariable("event_uid") UUID eventUid,
                                                        @RequestBody @Valid CreateEventOpenPositionDto createEventOpenPositionDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(eventPositionService.createInEvent(organizerUid, eventUid, createEventOpenPositionDto)
        .map(newPositionUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newPositionUid))
            .body(newPositionUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadEventOpenPositionDto>> findByUid(JwtAuthenticationToken token,
                                                                               @PathVariable("uid") UUID uid,
                                                                               @PathVariable("organizer_uid") UUID organizerUid,
                                                                               @PathVariable("event_uid") UUID eventUid) {
    return CompletableFuture.completedFuture(eventPositionService.findByUid(organizerUid, eventUid, uid)
        .map(positionUid -> ResponseEntity
            .ok()
            .body(positionUid)
        )
        .orElse(ResponseEntity.notFound().build())
    );
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token,
                                                        @PathVariable("uid") UUID uid,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @PathVariable("event_uid") UUID eventUid,
                                                        @RequestBody @Valid UpdateEventOpenPositionDto updateEventOpenPositionDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(eventPositionService.update(organizerUid, eventUid, uid, updateEventOpenPositionDto)
        .map(positionUid -> ResponseEntity
            .ok()
            .body(positionUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> delete(JwtAuthenticationToken token,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @PathVariable("event_uid") UUID eventUid,
                                                        @PathVariable("uid") UUID uid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (eventPositionService.delete(organizerUid, eventUid, uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
