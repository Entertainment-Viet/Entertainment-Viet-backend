package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.talent.boundary.TalentBoundary;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(path = TalentController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
public class TalentController {

  public static final String REQUEST_MAPPING_PATH = "/talents";

  private final TalentBoundary talentService;

  @GetMapping()
  public CompletableFuture<List<TalentDto>> findAll() {
    return CompletableFuture.completedFuture(talentService.findAll());
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<TalentDto> findByUid(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(talentService.findByUid(uid));
  }
}
