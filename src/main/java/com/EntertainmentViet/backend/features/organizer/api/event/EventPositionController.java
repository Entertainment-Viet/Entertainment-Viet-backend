package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.organizer.boundary.event.EventBookingBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = EventPositionController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class EventPositionController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events/{event_uid}/positions";
}
