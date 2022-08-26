package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.features.organizer.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.organizer.boundary.JobOfferBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
