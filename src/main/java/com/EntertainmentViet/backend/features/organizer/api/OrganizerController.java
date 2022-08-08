package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.features.organizer.boundary.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = OrganizerController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class OrganizerController {

  public static final String REQUEST_MAPPING_PATH = "/organizers";

  private final OrganizerBoundary organizerService;

  @GetMapping(value = "/{uid}")
  public OrganizerDto findById(@PathVariable("uid") Long uid) {
    return organizerService.findByUid(uid);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody @Valid OrganizerDto organizerDto) {
    return organizerService.create(organizerDto);
  }

  @PutMapping(value = "/{uid}")
  public Long updateById(@PathVariable("uid") Long uid) {
    // TODO
    return null;
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
