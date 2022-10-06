package com.EntertainmentViet.backend.features.organizer.api.joboffer;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.joboffer.JobOfferBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ListJobOfferParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.UpdateJobOfferDto;
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
@Validated
@RequestMapping(path = JobOfferController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class JobOfferController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/joboffers";

  private final JobOfferBoundary jobOfferService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadJobOfferDto>>> findByOrganizerUid(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid,
                                                                                           @ParameterObject Pageable pageable,
                                                                                           @ParameterObject ListJobOfferParamDto paramDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(RestUtils.toPageResponse(
        jobOfferService.findByOrganizerUid(organizerUid, paramDto, pageable)
    )));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadJobOfferDto>> findByUid(JwtAuthenticationToken token,
                                                                      @PathVariable("organizer_uid") UUID organizerUid,
                                                                      @PathVariable("uid") UUID uid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(jobOfferService.findByOrganizerUidAndUid(organizerUid, uid)
            .map( talentDto -> ResponseEntity
                    .ok()
                    .body(talentDto)
            )
            .orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @RequestBody @Valid CreateJobOfferDto createJobOfferDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(jobOfferService.create(createJobOfferDto, organizerUid)
            .map(newJobOfferUid -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newJobOfferUid))
                    .body(newJobOfferUid)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(value = "/{uid}",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @RequestBody @Valid UpdateJobOfferDto updateJobOfferDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(jobOfferService.update(updateJobOfferDto, organizerUid, uid)
            .map(newJobOfferUid -> ResponseEntity
                    .ok()
                    .body(newJobOfferUid)
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

    if (jobOfferService.delete(uid, organizerUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
