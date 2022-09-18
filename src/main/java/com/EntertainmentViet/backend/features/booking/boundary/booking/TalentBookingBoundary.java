package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;

import java.util.List;
import java.util.UUID;

public interface TalentBookingBoundary {

    List<ReadBookingDto> listBooking(UUID talentId);

    boolean acceptBooking(UUID talentId, UUID bookingId);

    boolean rejectBooking(UUID talentId, UUID bookingId);
}
