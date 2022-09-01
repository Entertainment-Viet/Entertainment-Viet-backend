package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.organizer.boundary.JobOfferBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
public class JobOfferController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/joboffers";

  private final JobOfferBoundary jobOfferService;

  @GetMapping()
  public CompletableFuture<List<JobOfferDto>> findByOrganizerUid(@PathVariable("organizer_uid") UUID organizerUid) {
    return CompletableFuture.completedFuture(jobOfferService.findByOrganizerUid(organizerUid));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<JobOfferDto>> findByOrganizerUid(@PathVariable("organizer_uid") UUID organizerUid,
                                                                          @PathVariable("uid") UUID uid) {
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
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @RequestBody @Valid JobOfferDto jobOfferDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid) {
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
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<UUID>> update(@RequestBody @Valid JobOfferDto jobOfferDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {
    return  CompletableFuture.completedFuture(jobOfferService.update(jobOfferDto, organizerUid, uid)
            .map(newJobOfferDto -> ResponseEntity
                    .ok()
                    .body(newJobOfferDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<Void>> delete(@PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {
    if (jobOfferService.delete(uid, organizerUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
