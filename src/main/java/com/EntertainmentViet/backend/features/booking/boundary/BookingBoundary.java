package com.EntertainmentViet.backend.features.booking.boundary;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;

import java.util.UUID;

public interface BookingBoundary {

    BookingDto findByUid(UUID uid);

    UUID create(BookingDto bookingDto) throws Exception;

    UUID update(BookingDto bookingDto, UUID uid) throws Exception;

}
