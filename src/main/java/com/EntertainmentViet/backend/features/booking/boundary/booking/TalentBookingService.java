package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public ListBookingResponseDto listBooking(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable) {
        var bookingList = bookingRepository.findByTalentUid(talentId, paramDto, pageable);
        var dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto)
            .collect(Collectors.toList());

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);
        var unpaidSum = bookingList.stream().findAny().map(Booking::getTalent).map(Talent::computeUnpaidSum).orElse(0.0);
        return ListBookingResponseDto.builder()
            .unpaidSum(BigDecimal.valueOf(unpaidSum))
            .bookings(RestUtils.toPageResponse(dataPage))
            .build();
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

    @Override
    public boolean finishBooking(UUID talentUid, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);

        if (!EntityValidationUtils.isBookingWithUid(booking, bookingId)) {
            return false;
        }
        if (!EntityValidationUtils.isBookingBelongToTalentWithUid(booking, talentUid)) {
            return false;
        }

        try {
            Talent talent = booking.getTalent();
            talent.finishBooking(bookingId);
            talentRepository.save(talent);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }
}
