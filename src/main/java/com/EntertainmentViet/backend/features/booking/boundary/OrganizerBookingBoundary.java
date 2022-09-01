package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;

import java.util.List;
import java.util.UUID;

public interface OrganizerBookingBoundary {

    List<BookingDto> listBooking(UUID organizerId);

    boolean acceptBooking(UUID organizerId, UUID bookingId);

    boolean rejectBooking(UUID organizerId, UUID bookingId);
}
