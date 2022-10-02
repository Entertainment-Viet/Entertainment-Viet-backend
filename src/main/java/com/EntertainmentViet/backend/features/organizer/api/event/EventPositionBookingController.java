package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.organizer.boundary.event.EventPositionBookingBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = EventPositionBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class EventPositionBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events/{event_uid}/positions/{position_id}/bookings";

  private final EventPositionBookingBoundary eventBookingService;
}
