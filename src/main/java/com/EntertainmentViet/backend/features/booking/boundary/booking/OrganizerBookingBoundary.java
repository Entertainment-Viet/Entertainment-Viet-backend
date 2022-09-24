package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrganizerBookingBoundary {

    Page<ReadBookingDto> listBooking(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable);

    boolean acceptBooking(UUID organizerId, UUID bookingId);

    boolean rejectBooking(UUID organizerId, UUID bookingId);
}
