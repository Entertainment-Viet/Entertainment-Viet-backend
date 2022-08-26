package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.booking.api.BookingController;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = PackageController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
public class PackageController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages";



}
