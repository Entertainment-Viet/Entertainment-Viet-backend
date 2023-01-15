package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.organizer.boundary.organizer.OrganizerService;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ListOrganizerParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = AdminOrganizerController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminOrganizerController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/organizers";
  public static final String DEACTIVE_PATH = "/deactive";

  private final UserBoundary userService;
  private final OrganizerService organizerService;

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> verify(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {

    try {
      if (userService.verifyOrganizer(uid)) {
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
      }
    } catch (KeycloakUnauthorizedException ex) {
      log.error("Can not verify organizer account in keycloak server", ex);
    }
    log.warn(String.format("Can not verify organizer account with id '%s'", uid));
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @GetMapping()
  public CompletableFuture<ResponseEntity<CustomPage<ReadOrganizerDto>>> findAll(
      @ParameterObject Pageable pageable, ListOrganizerParamDto paramDto) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        organizerService.findAll(pageable, paramDto)));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadOrganizerDto>> findById(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(organizerService.findByUid(uid, true)
        .map(locationDto -> ResponseEntity
            .ok()
            .body(locationDto))
        .orElse(ResponseEntity.notFound().build()));
  }

  @DeleteMapping(value = "/{uid}" + DEACTIVE_PATH)
  public CompletableFuture<ResponseEntity<Void>> delete(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {
    if (organizerService.delete(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> disapprove(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {
    if (organizerService.disapprove(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
