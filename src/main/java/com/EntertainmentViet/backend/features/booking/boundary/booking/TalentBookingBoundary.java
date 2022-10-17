package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TalentBookingBoundary {

  ListBookingResponseDto listBooking(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable);

  Optional<UUID> create(UUID talentUid, CreateBookingDto createBookingDto);

  Optional<UUID> update(UUID talentUid, UUID uid, UpdateBookingDto updateBookingDto);

  boolean acceptBooking(UUID talentId, UUID bookingId);

  boolean rejectBooking(UUID talentId, UUID bookingId);

  boolean finishBooking(UUID talentUid, UUID bookingUid);
}
