package com.EntertainmentViet.backend.features.talent.api.talent;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.talent.ReviewBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@Validated
@RequestMapping(path = TalentReviewController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class TalentReviewController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/reviews";

  private final ReviewBoundary reviewService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadReviewDto>>> findAll(@PathVariable("talent_uid") UUID talentUid,
                                                                              @ParameterObject Pageable pageable) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(reviewService.findAll(talentUid, pageable)));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadReviewDto>> findByUid(@PathVariable("talent_uid") UUID talentUid, @PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(reviewService.findByUidAndTalentUid(uid, talentUid)
        .map( reviewDto -> ResponseEntity
            .ok()
            .body(reviewDto)
        )
        .orElse( ResponseEntity.notFound().build()));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @PathVariable("talent_uid") UUID talentUid,
                                                       @RequestBody @Valid CreateReviewDto reviewDto) {

    return  CompletableFuture.completedFuture(reviewService.create(reviewDto, talentUid)
        .map(newReviewUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newReviewUid))
            .body(newReviewUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }
}
