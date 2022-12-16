package com.EntertainmentViet.backend.features.booking.boundary.booking;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.EntertainmentViet.backend.domain.businessLogic.FinanceUtils;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TalentBookingService implements TalentBookingBoundary {

    private final TalentRepository talentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;


    @Override
    public ListBookingResponseDto listBooking(boolean isOwnerUser, UUID talentId, ListTalentBookingParamDto paramDto,
            Pageable pageable) {
        var bookingList = bookingRepository.findByTalentUid(talentId, paramDto, pageable);
        var dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
            .toList();

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);
        var financeReport = FinanceUtils.generateOrganizerBookingReport(bookingList);
        return ListBookingResponseDto.builder()
            .unpaidSum(financeReport.getUnpaid())
            .price(financeReport.getPrice())
            .tax(financeReport.getTax())
            .total(financeReport.getTotal())
            .bookings(RestUtils.toPageResponse(dataPage))
            .build();
    }

    @Override
    public Optional<UUID> create(UUID talentUid, CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.fromCreateDtoToModel(createBookingDto);
        booking.setStatus(BookingStatus.ORGANIZER_PENDING);

        if (!EntityValidationUtils.isBookingValid(booking, createBookingDto.getOrganizerId(), createBookingDto.getTalentId())) {
            return Optional.empty();
        }
        if (!talentUid.equals(booking.getTalent().getUid())) {
            log.warn(String.format("Inconsistent input when creating a booking for talent '%s'", talentUid));
            return Optional.empty();
        }

        return Optional.ofNullable(bookingRepository.save(booking).getUid());
    }

    @Override
    public Optional<UUID> update(UUID talentUid, UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!talentUid.equals(updatingBooking.getTalent().getUid())) {
            log.warn(String.format("Can not find booking with id '%s' belong to talent with id '%s'", uid, talentUid));
            return Optional.empty();
        }

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.getTalent().updateBookingInfo(uid, newBookingData);

        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
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
