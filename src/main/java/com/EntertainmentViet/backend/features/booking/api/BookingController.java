package com.EntertainmentViet.backend.features.booking.api;

import com.EntertainmentViet.backend.features.booking.boundary.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = BookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
public class BookingController {

  public static final String REQUEST_MAPPING_PATH = "/bookings";

  private final BookingBoundary bookingService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<BookingDto> findByUid(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(bookingService.findByUid(uid));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<BookingDto>> create(HttpServletRequest request, @RequestBody @Valid BookingDto bookingDto) {
    var newBookingDto = bookingService.create(bookingDto);
    return  CompletableFuture.completedFuture(
            ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newBookingDto.getUid()))
                    .body(newBookingDto)
    );
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<BookingDto>> update(HttpServletRequest request,@PathVariable("uid") UUID uid, @RequestBody @Valid BookingDto bookingDto) throws Exception {
    try {
      var newBookingDto = bookingService.update(bookingDto, uid);
      return  CompletableFuture.completedFuture(
              ResponseEntity
                      .created(RestUtils.getCreatedLocationUri(request, newBookingDto.getUid()))
                      .body(newBookingDto)
      );
    } catch (Exception e) {
      throw e;
    }
  }
}
