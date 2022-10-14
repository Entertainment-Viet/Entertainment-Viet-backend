package com.EntertainmentViet.backend.features.booking.api.booking;

import com.EntertainmentViet.backend.exception.RollbackException;
import com.EntertainmentViet.backend.features.booking.boundary.booking.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.boundary.booking.OrganizerBookingBoundary;
import com.EntertainmentViet.backend.features.talent.boundary.talent.ReviewBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrganizerBookingControllerProxy {
  private final OrganizerBookingBoundary organizerBookingService;

  private final ReviewBoundary reviewService;

  @Transactional(rollbackFor = {RollbackException.class})
  public Optional<UUID> finishBooingAndReview(CreateReviewDto reviewDto, UUID organizerUid, UUID bookingUid) {
    Optional<UUID> result;
    if (!organizerBookingService.finishBooking(organizerUid, bookingUid)) {
      result = Optional.empty();
    } else {
      result = reviewService.addReviewToBooking(reviewDto, bookingUid);
    }
    if (result.isEmpty()) {
      throw new RollbackException();
    }
    return  result;
  }
}
