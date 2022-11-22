package com.EntertainmentViet.backend.features.booking.boundary.booking;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;

public interface BookingBoundary {

    Optional<ReadBookingDto> findByUid(boolean isCurrentUser, UUID ownerUid, UUID uid);
}
