package com.EntertainmentViet.backend.features.booking.boundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;

import java.util.Optional;
import java.util.UUID;

public interface BookingBoundary {

    Optional<BookingDto> findByUid(UUID ownerUid, UUID uid);

    Optional<UUID> create(UUID ownerUid, BookingDto bookingDto);

    Optional<UUID> update(UUID ownerUid, UUID uid, BookingDto bookingDto);

}
