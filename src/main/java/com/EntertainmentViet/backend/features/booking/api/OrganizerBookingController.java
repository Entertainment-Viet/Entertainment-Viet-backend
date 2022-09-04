package com.EntertainmentViet.backend.features.booking.api;

import com.EntertainmentViet.backend.features.booking.boundary.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.booking.boundary.OrganizerBookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = OrganizerBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class OrganizerBookingController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/bookings";

  private final OrganizerBookingBoundary organizerBookingService;

  private final BookingBoundary bookingService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<BookingDto>> findByUid(@PathVariable("organizer_uid") UUID organizer_uid, @PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(bookingService.findByUid(organizer_uid, uid)
        .map(bookingDto -> ResponseEntity
            .ok()
            .body(bookingDto)
        ).orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @PathVariable("organizer_uid") UUID organizer_uid,
                                                        @RequestBody @Valid BookingDto bookingDto) {
    return  CompletableFuture.completedFuture(bookingService.create(organizer_uid, bookingDto)
        .map(newBookingDto -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newBookingDto))
            .body(newBookingDto)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<UUID>> update(HttpServletRequest request, @PathVariable("uid") UUID uid,
                                                        @PathVariable("organizer_uid") UUID organizer_uid,
                                                        @RequestBody @Valid BookingDto bookingDto) {
    return  CompletableFuture.completedFuture(bookingService.update(organizer_uid, uid, bookingDto)
        .map(newBookingDto -> ResponseEntity
            .ok()
            .body(newBookingDto)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }
  @GetMapping()
  public CompletableFuture<List<BookingDto>> listBooking(@PathVariable("organizer_uid") UUID organizerId) {
    return CompletableFuture.completedFuture(organizerBookingService.listBooking(organizerId));
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(
          @PathVariable("organizer_uid") UUID organizerId,
          @PathVariable("uid") UUID bookingId) {
    if (organizerBookingService.acceptBooking(organizerId, bookingId)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(
          @PathVariable("organizer_uid") UUID organizerId,
          @PathVariable("uid") UUID bookingId) {
    if (organizerBookingService.rejectBooking(organizerId, bookingId)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
