package com.EntertainmentViet.backend.features.talent.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = PackageBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class PackageBookingController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages/{package_uid}/bookings";



}
