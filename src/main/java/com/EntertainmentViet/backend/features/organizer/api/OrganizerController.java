package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = OrganizerController.REQUEST_MAPPING_PATH)
public class OrganizerController {

  public static final String REQUEST_MAPPING_PATH = "/organizers";

  private final OrganizerBoundary organizerService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<OrganizerDto>> findByUid(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(organizerService.findByUid(uid)
        .map( organizerDto -> ResponseEntity
            .ok()
            .body(organizerDto)
        )
        .orElse( ResponseEntity.notFound().build())
    );
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @RequestBody @Valid OrganizerDto organizerDto) {
    return  CompletableFuture.completedFuture(organizerService.create(organizerDto)
        .map(newOrganizerUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newOrganizerUid))
            .body(newOrganizerUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
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
