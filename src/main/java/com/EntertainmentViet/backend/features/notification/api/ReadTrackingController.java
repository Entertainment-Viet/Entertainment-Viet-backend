package com.EntertainmentViet.backend.features.notification.api;

import com.EntertainmentViet.backend.domain.entities.notification.ReadTrackingNotification;
import com.EntertainmentViet.backend.features.notification.boundary.ReadTrackingBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = ReadTrackingController.REQUEST_MAPPING_PATH)
public class ReadTrackingController {

  public static final String REQUEST_MAPPING_PATH = "/notify/{account_id}/read";

  private final ReadTrackingBoundary readTrackingService;

  @PostMapping
  public void updateLastRead(@PathVariable("account_id") UUID account_id, @RequestBody @Valid ReadTrackingNotification readTrackingNotification) {
    readTrackingService.sendNotification(account_id, readTrackingNotification);
  }
}
