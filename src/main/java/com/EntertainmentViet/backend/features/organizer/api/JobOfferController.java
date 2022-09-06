package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.organizer.boundary.JobOfferBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@Validated
@RequestMapping(path = JobOfferController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class JobOfferController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/joboffers";

  private final JobOfferBoundary jobOfferService;

  @GetMapping()
  public CompletableFuture<ResponseEntity<List<JobOfferDto>>> findByOrganizerUid(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid) {

    if (organizerUid != RestUtils.getUidFromToken(token)) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(jobOfferService.findByOrganizerUid(organizerUid)));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<JobOfferDto>> findByUid(JwtAuthenticationToken token,
                                                                           @PathVariable("organizer_uid") UUID organizerUid,
                                                                          @PathVariable("uid") UUID uid) {

    if (organizerUid != RestUtils.getUidFromToken(token)) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(jobOfferService.findByOrganizerUidAndUid(organizerUid, uid)
            .map( talentDto -> ResponseEntity
                    .ok()
                    .body(talentDto)
            )
            .orElse( ResponseEntity.notFound().build()));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @RequestBody @Valid JobOfferDto jobOfferDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid) {

    if (organizerUid != RestUtils.getUidFromToken(token)) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(jobOfferService.create(jobOfferDto, organizerUid)
            .map(newJobOfferDto -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newJobOfferDto))
                    .body(newJobOfferDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @RequestBody @Valid JobOfferDto jobOfferDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {

    if (organizerUid != RestUtils.getUidFromToken(token)) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(jobOfferService.update(jobOfferDto, organizerUid, uid)
            .map(newJobOfferDto -> ResponseEntity
                    .ok()
                    .body(newJobOfferDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> delete(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {

    if (organizerUid != RestUtils.getUidFromToken(token)) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (jobOfferService.delete(uid, organizerUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
