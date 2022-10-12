package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrganizerBookingBoundary {

    ListBookingResponseDto listBooking(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable);

    boolean acceptBooking(UUID organizerId, UUID bookingId);

    boolean rejectBooking(UUID organizerId, UUID bookingId);
}
