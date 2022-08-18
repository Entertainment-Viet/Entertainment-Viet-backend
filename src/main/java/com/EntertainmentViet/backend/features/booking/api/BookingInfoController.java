package com.EntertainmentViet.backend.features.booking.api;

import com.EntertainmentViet.backend.features.booking.boundary.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = BookingInfoController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
public class BookingInfoController {

  public static final String REQUEST_MAPPING_PATH = "/bookings";

  private final BookingBoundary bookingService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<BookingDto> findByUid(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(bookingService.findByUid(uid));
  }
}
