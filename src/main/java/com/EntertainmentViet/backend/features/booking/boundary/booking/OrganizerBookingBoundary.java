package com.EntertainmentViet.backend.features.booking.boundary.booking;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import org.springframework.data.domain.Pageable;

public interface OrganizerBookingBoundary {

  ListBookingResponseDto listBooking(boolean isOwnerUser, UUID organizerId, ListOrganizerBookingParamDto paramDto,
          Pageable pageable);

  Optional<UUID> create(UUID organizerUid, CreateBookingDto createBookingDto);

  Optional<UUID> update(UUID organizerUid, UUID uid, UpdateBookingDto updateBookingDto);

  boolean acceptBooking(UUID organizerId, UUID bookingId);

  boolean rejectBooking(UUID organizerId, UUID bookingId);

  boolean finishBooking(UUID organizerUid, UUID bookingUid);

  Optional<UUID> finishBooingAndReview(CreateReviewDto reviewDto, UUID organizerUid, UUID bookingUid);
}
