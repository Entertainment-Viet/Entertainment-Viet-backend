package com.EntertainmentViet.backend.features.booking.boundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;

import java.util.UUID;

public interface BookingBoundary {

    BookingDto findByUid(UUID uid);

    BookingDto create(BookingDto bookingDto);

    BookingDto update(BookingDto bookingDto, UUID uid);

}
