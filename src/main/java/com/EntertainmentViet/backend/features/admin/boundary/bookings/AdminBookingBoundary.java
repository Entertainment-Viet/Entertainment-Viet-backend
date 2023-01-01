package com.EntertainmentViet.backend.features.admin.boundary.bookings;

import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.*;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AdminBookingBoundary {

    Optional<ReadBookingDto> findByUid(UUID uid);

    AdminListBookingResponseDto listOrganizerBooking(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable);

    AdminListBookingResponseDto listTalentBooking(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable);

    AdminListBookingResponseDto listEventBooking(UUID eventUid, ListEventBookingParamDto paramDto, Pageable pageable);

    Optional<UUID> update(UUID uid, UpdateBookingDto updateBookingDto);
}
