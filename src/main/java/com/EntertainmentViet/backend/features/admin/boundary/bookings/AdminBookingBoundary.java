package com.EntertainmentViet.backend.features.admin.boundary.bookings;

import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingParamDto;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AdminBookingBoundary {

    Optional<ReadBookingDto> findByUid(boolean isCurrentUser, UUID uid);

    AdminListBookingResponseDto listBooking(boolean isCurrentUser, AdminListBookingParamDto paramDto, Pageable pageable);

    Optional<UUID> update(UUID uid, UpdateBookingDto updateBookingDto);
}
