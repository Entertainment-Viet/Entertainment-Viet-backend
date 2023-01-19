package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.DetailBookingResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface BookingBoundary {

    Optional<DetailBookingResponseDto> findByUid(boolean isOwnerUser, UUID ownerUid, UUID uid);
}
