package com.EntertainmentViet.backend.features.booking.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = OrganizerBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class OrganizerBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/bookings";



}
