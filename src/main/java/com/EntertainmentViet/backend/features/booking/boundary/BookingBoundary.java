package com.EntertainmentViet.backend.features.booking.boundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;

import java.util.Optional;
import java.util.UUID;

public interface BookingBoundary {

    Optional<BookingDto> findByUid(UUID uid);

    Optional<UUID> create(BookingDto bookingDto);

    Optional<UUID> update(BookingDto bookingDto, UUID uid);

}
