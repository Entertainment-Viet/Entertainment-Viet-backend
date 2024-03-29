package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.exception.KeycloakUserConflictException;
import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.EntertainmentViet.backend.exception.rest.SystemUnavailableException;
import com.EntertainmentViet.backend.exception.rest.WrongSystemConfigurationException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.CreatedOrganizerDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreatedTalentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = UserController.REQUEST_MAPPING_PATH)
@Async
@RequiredArgsConstructor
@Slf4j
public class UserController {

  public static final String REQUEST_MAPPING_PATH = "/users";

  public static final String ORGANIZER_PATH = "/organizers";
  public static final String TALENT_PATH = "/talents";

  private final UserBoundary userService;

  @PostMapping(value = ORGANIZER_PATH,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> createOrganizer(HttpServletRequest request, @RequestBody @Valid CreatedOrganizerDto createdOrganizerDto) {
    try {
      return CompletableFuture.completedFuture(userService.createOrganizer(createdOrganizerDto)
          .map(newOrganizerUid -> ResponseEntity
              .created(RestUtils.getCreatedLocationUri(request, newOrganizerUid))
              .body(newOrganizerUid)
          )
          .orElse(ResponseEntity.badRequest().build())
      );
    } catch (KeycloakUnauthorizedException ex) {
      throw new WrongSystemConfigurationException("Can not create organizer", ex);
    } catch (KeycloakUserConflictException ex) {
      throw new InvalidInputException("Same organizer already exists. Please provide different username and email");
    }
  }

  @PostMapping(value = TALENT_PATH,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> createTalent(HttpServletRequest request, @RequestBody @Valid CreatedTalentDto createdTalentDto) {
    try {
      return CompletableFuture.completedFuture(userService.createTalent(createdTalentDto)
          .map(newTalentUid -> ResponseEntity
              .created(RestUtils.getCreatedLocationUri(request, newTalentUid))
              .body(newTalentUid)
          )
          .orElse(ResponseEntity.badRequest().build())
      );
    } catch (KeycloakUserConflictException ex) {
      throw new InvalidInputException("Same talent already exists. Please provide different username and email");
    } catch (KeycloakUnauthorizedException ex) {
      throw new SystemUnavailableException("Keycloak server unreachable", ex);
    }
  }

}
