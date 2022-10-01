package com.EntertainmentViet.backend.features.organizer.api.event;

import com.EntertainmentViet.backend.features.organizer.boundary.event.EventBookingBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = EventPositionBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class EventPositionBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/events/{event_uid}/positions/{position_id}/bookings";

  private final EventBookingBoundary eventBookingService;
}
