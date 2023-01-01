package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.QueryParamsUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.event.EventBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = EventController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventController {

  public static final String REQUEST_MAPPING_PATH = "/events";

  private final EventBoundary eventService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadEventDto>>> findAll(@ParameterObject Pageable pageable,
                                                                             @ParameterObject ListEventParamDto paramDto) {
    if (QueryParamsUtils.isInvalidParams(paramDto)) {
      log.warn(String.format("Currency is not provided '%s'", paramDto.getCurrency()));
      return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(eventService.findAll(paramDto, pageable)));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadEventDto>> findByUid(JwtAuthenticationToken token,
                                                                   @PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(eventService.findByUid(uid)
        .map( eventDto -> ResponseEntity
            .ok()
            .body(eventDto)
        )
        .orElse(ResponseEntity.notFound().build())
    );
  }
}
