package com.EntertainmentViet.backend.features.notification.api;

import com.EntertainmentViet.backend.domain.entities.notification.BookingNotification;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.notification.boundary.BookingNotifyBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping(path = BookingNotificationController.REQUEST_MAPPING_PATH)
@Async
@RequiredArgsConstructor
@Validated
public class BookingNotificationController {

  public static final String REQUEST_MAPPING_PATH = "/notify/{account_id}/booking";

  private final BookingNotifyBoundary bookingNotifyService;

  @GetMapping("/list")
  @ResponseBody
  public CompletableFuture<ResponseEntity<CustomPage<BookingNotification>>> getUnreadNotification(@PathVariable("account_id") UUID account_id, Pageable pageable) {
    return CompletableFuture.completedFuture(
        ResponseEntity.ok().body(RestUtils.toLazyLoadPageResponse(bookingNotifyService.getAllNotification(account_id, pageable))));
  }

  @GetMapping("/read")
  @ResponseBody
  public void markNotificationAsRead(@PathVariable("account_id") UUID account_id) {
    bookingNotifyService.updateIsRead(account_id, true);
  }
}
