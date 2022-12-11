package com.EntertainmentViet.backend.features.talent.api.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Async
@RequestMapping(path = AdvertisementController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class AdvertisementController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/advertisements";

  public static final String CASH_PATH = "/cash";

}
