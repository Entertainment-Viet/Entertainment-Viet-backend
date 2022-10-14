package com.EntertainmentViet.backend.features.booking.boundary.booking;
import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;

import java.util.Optional;
import java.util.UUID;

public interface BookingBoundary {

    Optional<ReadBookingDto> findByUid(UUID ownerUid, UUID uid);

    Optional<UUID> createForOrganizer(UUID organizerUid, CreateBookingDto createBookingDto);

    Optional<UUID> createForTalent(UUID talentUid, CreateBookingDto createBookingDto);

    Optional<UUID> updateFromOrganizer(UUID organizerUid, UUID uid, UpdateBookingDto updateBookingDto);

    Optional<UUID> updateFromTalent(UUID talentUid, UUID uid, UpdateBookingDto updateBookingDto);

}
