package com.EntertainmentViet.backend.features.talent.api.feedback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Async
@Validated
@RequestMapping(path = TalentFeedbackController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class TalentFeedbackController {

  public static final String REQUEST_MAPPING_PATH = "/talent/{talent_uid}/feedbacks";

}
