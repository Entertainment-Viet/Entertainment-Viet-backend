package com.EntertainmentViet.backend.features.organizer.api.feedback;

import com.EntertainmentViet.backend.features.organizer.api.joboffer.JobOfferController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Async
@Validated
@RequestMapping(path = OrganizerFeedbackController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class OrganizerFeedbackController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/feedbacks";

}
