package com.EntertainmentViet.backend.features.admin.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Async
@RequestMapping(path = AdminEventController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminEventController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/events";


}
