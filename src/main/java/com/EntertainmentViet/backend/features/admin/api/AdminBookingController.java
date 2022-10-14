package com.EntertainmentViet.backend.features.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = AdminBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class AdminBookingController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/bookings";


}
