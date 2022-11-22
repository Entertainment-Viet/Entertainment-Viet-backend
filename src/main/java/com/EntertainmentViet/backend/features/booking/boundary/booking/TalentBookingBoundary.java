package com.EntertainmentViet.backend.features.booking.boundary.booking;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import org.springframework.data.domain.Pageable;

public interface TalentBookingBoundary {

  ListBookingResponseDto listBooking(boolean isCurrentUser, UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable);

  Optional<UUID> create(UUID talentUid, CreateBookingDto createBookingDto);

  Optional<UUID> update(UUID talentUid, UUID uid, UpdateBookingDto updateBookingDto);

  boolean acceptBooking(UUID talentId, UUID bookingId);

  boolean rejectBooking(UUID talentId, UUID bookingId);

  boolean finishBooking(UUID talentUid, UUID bookingUid);
}
