package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
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
public class TalentBookingService implements TalentBookingBoundary {

    private final TalentRepository talentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;


    @Override
    public Page<ReadBookingDto> listBooking(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable) {
        var dtoList = bookingRepository.findByTalentUid(talentId, paramDto, pageable).stream()
            .map(bookingMapper::toReadDto)
            .collect(Collectors.toList());

        return RestUtils.getPageEntity(dtoList, pageable);
    }

    @Override
    public boolean acceptBooking(UUID talentId, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);
        if (!EntityValidationUtils.isBookingWithUid(booking, bookingId)) {
            return false;
        }

        if (!EntityValidationUtils.isBookingBelongToTalentWithUid(booking, talentId)) {
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
        if (!EntityValidationUtils.isBookingWithUid(booking, bookingId)) {
            return false;
        }

        if (!EntityValidationUtils.isBookingBelongToTalentWithUid(booking, talentId)) {
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
}
