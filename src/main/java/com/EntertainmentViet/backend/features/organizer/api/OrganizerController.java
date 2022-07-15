package com.EntertainmentViet.backend.features.organizer.api;

import com.EntertainmentViet.backend.features.organizer.boundary.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/organizers")
@RequiredArgsConstructor
public class OrganizerController {

  private final OrganizerBoundary organizerService;

  @GetMapping(value = "/{id}")
  public OrganizerDto findById(@PathVariable("id") Long id) {
    return organizerService.findById(id);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody @Valid OrganizerDto organizerDto) {
    return organizerService.create(organizerDto);
  }
}
