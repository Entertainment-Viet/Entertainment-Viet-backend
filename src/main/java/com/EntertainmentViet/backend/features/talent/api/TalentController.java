package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.TalentBoundary;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping(path = TalentController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
public class TalentController {

  public static final String REQUEST_MAPPING_PATH = "/talents";

  private final TalentBoundary talentService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<TalentDto>> findByUid(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(talentService.findByUid(uid)
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
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @RequestBody @Valid TalentDto talentDto) {
    return  CompletableFuture.completedFuture(talentService.create(talentDto)
            .map(newTalentUid -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newTalentUid))
                    .body(newTalentUid)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<UUID>> update(HttpServletRequest request,@PathVariable("uid") UUID uid, @RequestBody @Valid TalentDto talentDto) {
    return  CompletableFuture.completedFuture(talentService.update(talentDto, uid)
            .map(updatedTalentUid -> ResponseEntity
                    .ok()
                    .body(updatedTalentUid)
            ).orElse(ResponseEntity.badRequest().build())
    );
  }
}
