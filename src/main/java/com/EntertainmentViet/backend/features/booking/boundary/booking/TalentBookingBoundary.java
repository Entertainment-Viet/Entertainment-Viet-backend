package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TalentBookingBoundary {

    Page<ReadBookingDto> listBooking(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable);

    boolean acceptBooking(UUID talentId, UUID bookingId);

    boolean rejectBooking(UUID talentId, UUID bookingId);
}
