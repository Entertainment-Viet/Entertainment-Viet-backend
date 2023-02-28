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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping(path = BookingNotificationController.REQUEST_MAPPING_PATH)
@Async
@RequiredArgsConstructor
@Validated
public class BookingNotificationController {

  public static final String REQUEST_MAPPING_PATH = "/notify/{account_id}/booking";

  public static final String LIST_PATH = "/list";
  public static final String COUNT_PATH = "/count";
  public static final String READ_PATH = "/read";

  private final BookingNotifyBoundary bookingNotifyService;


  @GetMapping(LIST_PATH)
  @ResponseBody
  public CompletableFuture<ResponseEntity<CustomPage<BookingNotification>>> getAllNotification(@PathVariable("account_id") UUID account_id, Pageable pageable) {
    return CompletableFuture.completedFuture(
        ResponseEntity.ok().body(RestUtils.toLazyLoadPageResponse(bookingNotifyService.getAllNotification(account_id, pageable))));
  }

  @GetMapping(COUNT_PATH)
  @ResponseBody
  public CompletableFuture<ResponseEntity<Integer>> getUnreadNotificationCount(@PathVariable("account_id") UUID account_id) {
    return CompletableFuture.completedFuture(
        ResponseEntity.ok().body(bookingNotifyService.countUnreadNotification(account_id)));
  }

  @PostMapping(READ_PATH)
  @ResponseBody
  public void markNotificationAsRead(@PathVariable("account_id") UUID account_id) {
    bookingNotifyService.updateIsRead(account_id, true);
  }
}
