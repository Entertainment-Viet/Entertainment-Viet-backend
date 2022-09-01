package com.EntertainmentViet.backend.features.booking.api;

import com.EntertainmentViet.backend.features.booking.boundary.OrganizerBookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import lombok.RequiredArgsConstructor;
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

  @GetMapping()
  public CompletableFuture<List<BookingDto>> listBooking(@PathVariable("organizer_uid") UUID organizerId) {
    return CompletableFuture.completedFuture(organizerBookingService.listBooking(organizerId));
  }

  @PostMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(
          @PathVariable("organizer_uid") UUID organizerId,
          @PathVariable("uid") UUID bookingId) {
    if (organizerBookingService.acceptBooking(organizerId, bookingId)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(
          @PathVariable("organizer_uid") UUID organizerId,
          @PathVariable("uid") UUID bookingId) {
    if (organizerBookingService.rejectBooking(organizerId, bookingId)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
