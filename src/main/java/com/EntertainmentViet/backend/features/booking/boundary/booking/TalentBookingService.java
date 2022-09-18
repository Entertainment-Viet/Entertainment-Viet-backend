package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
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
public class TalentBookingService implements TalentBookingBoundary {

    private final TalentRepository talentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;


    @Override
    public List<ReadBookingDto> listBooking(UUID talentId) {
        return talentRepository.findByUid(talentId)
            .map(organizer -> organizer.getBookings().stream().map(bookingMapper::toReadDto).collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    @Override
    public boolean acceptBooking(UUID talentId, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (!isBookingWithUid(booking, bookingId)) {
            return false;
        }

        if (!isBookingBelongToTalentWithUid(booking, talentId)) {
            return false;
        }

        try {
            Talent talent = booking.getTalent();
            talent.acceptBooking(bookingId);
            talentRepository.save(talent);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean rejectBooking(UUID talentId, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (!isBookingWithUid(booking, bookingId)) {
            return false;
        }

        if (!isBookingBelongToTalentWithUid(booking, talentId)) {
            return false;
        }

        try {
            Talent talent = booking.getTalent();
            talent.rejectBooking(bookingId);
            talentRepository.save(talent);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean isBookingWithUid(Booking booking, UUID uid) {
        if (booking == null) {
            log.warn(String.format("Can not find booking with id '%s' ", uid));
            return false;
        }
        return true;
    }

    private boolean isBookingBelongToTalentWithUid(Booking booking, UUID talentUid) {
        if (booking.getTalent() == null) {
            log.warn(String.format("Can not find talent owning the booking with id '%s'", booking.getUid()));
            return false;
        }

        Talent talent = booking.getTalent();
        if (!talent.getUid().equals(talentUid)) {
            log.warn(String.format("Can not find any booking with id '%s' belong to talent with id '%s'", booking.getUid(), talentUid));
            return false;
        }
        return true;
    }
}
