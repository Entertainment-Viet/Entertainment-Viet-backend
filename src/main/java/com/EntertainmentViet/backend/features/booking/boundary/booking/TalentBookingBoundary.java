package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TalentBookingBoundary {

    ListBookingResponseDto listBooking(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable);

    boolean acceptBooking(UUID talentId, UUID bookingId);

    boolean rejectBooking(UUID talentId, UUID bookingId);
}
