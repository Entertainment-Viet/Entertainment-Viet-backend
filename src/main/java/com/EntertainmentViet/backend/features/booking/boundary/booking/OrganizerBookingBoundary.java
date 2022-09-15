package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;

import java.util.List;
import java.util.UUID;

public interface OrganizerBookingBoundary {

    List<ReadBookingDto> listBooking(UUID organizerId);

    boolean acceptBooking(UUID organizerId, UUID bookingId);

    boolean rejectBooking(UUID organizerId, UUID bookingId);
}
