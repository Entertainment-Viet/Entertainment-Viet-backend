package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.exception.InconsistentDataException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.boundary.talent.ReviewBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerBookingService implements OrganizerBookingBoundary {

    private final OrganizerRepository organizerRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final ReviewBoundary reviewService;

    @Override
    public ListBookingResponseDto listBooking(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable) {
        var bookingList = bookingRepository.findByOrganizerUid(organizerId, paramDto, pageable);
        var dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto)
            .collect(Collectors.toList());

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);
        var unpaidSum = bookingList.stream().findAny().map(Booking::getOrganizer).map(Organizer::computeUnpaidSum).orElse(0.0);
        return ListBookingResponseDto.builder()
            .unpaidSum(BigDecimal.valueOf(unpaidSum))
            .bookings(RestUtils.toPageResponse(dataPage))
            .build();
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

    @Override
    public boolean finishBooking(UUID organizerId, UUID bookingId) {
        Booking booking = bookingRepository.findByUid(bookingId).orElse(null);

        if (!EntityValidationUtils.isBookingWithUid(booking, bookingId)) {
            return false;
        }
        if (!EntityValidationUtils.isBookingBelongToOrganizerWithUid(booking, organizerId)) {
            return false;
        }

        try {
            Organizer organizer = booking.getOrganizer();
            organizer.finishBooking(bookingId);
            organizerRepository.save(organizer);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = {InconsistentDataException.class})
    public Optional<UUID> finishBooingAndReview(CreateReviewDto reviewDto, UUID organizerUid, UUID bookingUid) {
        Optional<UUID> result;
        if (!finishBooking(organizerUid, bookingUid)) {
            result = Optional.empty();
        } else {
            result = reviewService.addReviewToBooking(reviewDto, bookingUid);
        }
        if (result.isEmpty()) {
            throw new InconsistentDataException();
        }
        return  result;
    }
}
