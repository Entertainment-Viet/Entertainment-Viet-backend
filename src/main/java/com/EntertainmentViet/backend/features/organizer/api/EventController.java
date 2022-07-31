package com.EntertainmentViet.backend.features.organizer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = EventController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class EventController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events";

}
