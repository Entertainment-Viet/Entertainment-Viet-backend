package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = AdminOrganizerController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class AdminOrganizerController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/organizers";

  public static final String DEACTIVE_PATH = "/deactive";

  private final UserBoundary userService;


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
}
