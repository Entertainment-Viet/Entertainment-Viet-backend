package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerBookingService implements OrganizerBookingBoundary {

    private final OrganizerRepository organizerRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    @Override
    public Page<ReadBookingDto> listBooking(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable) {
        var dtoList = bookingRepository.findByOrganizerUid(organizerId, paramDto, pageable).stream()
            .map(bookingMapper::toReadDto)
            .collect(Collectors.toList());

        return RestUtils.getPageEntity(dtoList, pageable);
    }

    @Override
    public boolean acceptBooking(UUID organizerId, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (!EntityValidationUtils.isBookingWithUid(booking, bookingId)) {
            return false;
        }

        if (!EntityValidationUtils.isBookingBelongToOrganizerWithUid(booking, organizerId)) {
            return false;
        }

        try {
            Organizer organizer = booking.getOrganizer();
            organizer.acceptBooking(bookingId);
            organizerRepository.save(organizer);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean rejectBooking(UUID organizerId, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (!EntityValidationUtils.isBookingWithUid(booking, bookingId)) {
            return false;
        }

        if (!EntityValidationUtils.isBookingBelongToOrganizerWithUid(booking, organizerId)) {
            return false;
        }

        try {
            Organizer organizer = booking.getOrganizer();
            organizer.rejectBooking(bookingId);
            organizerRepository.save(organizer);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }
}
