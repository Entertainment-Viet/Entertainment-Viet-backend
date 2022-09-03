package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.organizer.boundary.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = OrganizerController.REQUEST_MAPPING_PATH)
@Slf4j
public class OrganizerController {

  public static final String REQUEST_MAPPING_PATH = "/organizers";

  private final OrganizerBoundary organizerService;

  private final UserBoundary userService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<OrganizerDto>> findByUid(@PathVariable("uid") UUID uid, JwtAuthenticationToken authentication) {
    return CompletableFuture.completedFuture(organizerService.findByUid(uid)
        .map( organizerDto -> ResponseEntity
            .ok()
            .body(organizerDto)
        )
        .orElse( ResponseEntity.notFound().build())
    );
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> verify(@PathVariable("uid") UUID uid) {
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

  @PutMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> updateById(@PathVariable("uid") UUID uid, @RequestBody @Valid OrganizerDto organizerDto) {
    return  CompletableFuture.completedFuture(organizerService.update(organizerDto, uid)
        .map(updatedOrganizerUid -> ResponseEntity
            .ok()
            .body(updatedOrganizerUid)
        ).orElse(ResponseEntity.badRequest().build())
    );
  }

  @GetMapping(value = "/{uid}/cash")
  public Long receivePayment(@PathVariable("uid") Long uid) {
    // TODO
    return null;
  }

  @PostMapping(value = "/{uid}/cash")
  public Long proceedPayment(@PathVariable("uid") Long uid) {
    // TODO
    return null;
  }

}
