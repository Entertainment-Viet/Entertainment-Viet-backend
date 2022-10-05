package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreatePositionApplicantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventPositionBookingBoundary {
  Page<ReadBookingDto> findByPositionUid(UUID organizerUid, UUID eventUid, UUID positionUid, ListOrganizerBookingParamDto paramDto, Pageable pageable);

  Optional<UUID> create(UUID organizerUid, UUID eventUid, UUID positionUid, CreatePositionApplicantDto createPositionApplicantDto);

  boolean acceptBooking(UUID organizerUid, UUID eventUid, UUID positionUid, UUID bookingUid);

  boolean rejectBooking(UUID organizerUid, UUID eventUid, UUID positionUid, UUID bookingUid);
}
