package com.EntertainmentViet.backend.features.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = AdminAdvertisementController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class AdminAdvertisementController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/advertisements";



}
