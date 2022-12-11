package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.organizer.OrganizerService;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

@RestController
@RequestMapping(path = AdminOrganizerController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
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
      @ParameterObject Pageable pageable) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        organizerService.findAll(pageable)));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadOrganizerDto>> findById(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(organizerService.findByUid(uid)
        .map(locationDto -> ResponseEntity
            .ok()
            .body(locationDto))
        .orElse(ResponseEntity.notFound().build()));
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> updateById(JwtAuthenticationToken token,
      @PathVariable("admin_uid") UUID adminUid,
      @PathVariable("uid") UUID uid,
      @RequestBody @Valid UpdateOrganizerDto updateOrganizerDto) {

    if (!adminUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of admin with uid '%s'",
          adminUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(organizerService.update(updateOrganizerDto, uid)
        .map(updatedTalent -> ResponseEntity
            .ok()
            .body(updatedTalent))
        .orElse(ResponseEntity.badRequest().build()));
  }
}
