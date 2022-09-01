package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.booking.dao.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
    public List<BookingDto> listBooking(UUID organizerId) {
        Organizer organizer = organizerRepository.findByUid(organizerId).orElse(null);
        if (isOrganizerWithUid(organizer)) {
            return organizer.getBookings().stream().map(bookingMapper::toDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean acceptBooking(UUID organizerId, UUID bookingId) {
        Organizer organizer = organizerRepository.findByUid(organizerId).orElse(null);
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (isOrganizerWithUid(organizer)) {
            if (booking.getOrganizer().getUid().equals(organizerId)) {
                if (organizer.acceptBooking(bookingId)) {
                    organizerRepository.save(organizer);
                    return true;
                }
            }
            log.warn(String.format("Booking not in organizer with id '%s' ", organizer.getUid()));
            return false;
        }
        return false;
    }

    @Override
    public boolean rejectBooking(UUID organizerId, UUID bookingId) {
        Organizer organizer = organizerRepository.findByUid(organizerId).orElse(null);
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (isOrganizerWithUid(organizer)) {
            if (booking.getOrganizer().getUid().equals(organizerId)) {
                if (organizer.rejectBooking(bookingId)) {
                    organizerRepository.save(organizer);
                    return true;
                }
            }
            log.warn(String.format("Booking not in organizer with id '%s' ", organizer.getUid()));
            return false;
        }
        return false;
    }

    private boolean isOrganizerWithUid(Organizer organizer) {
        if (organizer == null) {
            log.warn(String.format("Can not find package with id '%s' ", organizer.getUid()));
            return false;
        }
        return true;
    }
}
