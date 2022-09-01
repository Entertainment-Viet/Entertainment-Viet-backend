package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = UserController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class UserController {

  public static final String REQUEST_MAPPING_PATH = "/users";

  private final UserBoundary userService;

  @PostMapping(value = "/organizers",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<UUID>> createOrganizer(HttpServletRequest request, @RequestBody @Valid OrganizerDto organizerDto) {
    try {
      return CompletableFuture.completedFuture(userService.createOrganizer(organizerDto)
          .map(newOrganizerUid -> ResponseEntity
              .created(RestUtils.getCreatedLocationUri(request, newOrganizerUid))
              .body(newOrganizerUid)
          )
          .orElse(ResponseEntity.badRequest().build())
      );
    } catch (KeycloakUnauthorizedException | KeycloakUserConflictException ex) {
      log.error("Can not create organizer", ex);
      return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
  }

  @PostMapping(value = "/talents",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<UUID>> createTalent(HttpServletRequest request, @RequestBody @Valid TalentDto talentDto) {
    try {
      return CompletableFuture.completedFuture(userService.createTalent(talentDto)
          .map(newTalentUid -> ResponseEntity
              .created(RestUtils.getCreatedLocationUri(request, newTalentUid))
              .body(newTalentUid)
          )
          .orElse(ResponseEntity.badRequest().build())
      );
    } catch (KeycloakUserConflictException ex) {
      log.error("Can not create organizer due to username conflict", ex);
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CONFLICT).build());
    } catch (KeycloakUnauthorizedException ex) {
      log.error("Keycloak server unreachable", ex);
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_GATEWAY).build());
    }
  }

}
