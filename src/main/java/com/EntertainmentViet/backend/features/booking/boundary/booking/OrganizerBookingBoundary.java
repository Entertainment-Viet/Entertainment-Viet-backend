package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.DoubleStream;

public interface OrganizerBookingBoundary {

  ListBookingResponseDto listBooking(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable);

  boolean acceptBooking(UUID organizerId, UUID bookingId);

  boolean rejectBooking(UUID organizerId, UUID bookingId);

  boolean finishBooking(UUID organizerUid, UUID bookingUid);
}
