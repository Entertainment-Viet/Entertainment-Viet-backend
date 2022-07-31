package com.EntertainmentViet.backend.features.organizer.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = JobOfferController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class JobOfferController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/joboffers";

}