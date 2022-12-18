package com.EntertainmentViet.backend.features.admin.boundary.bookings;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingParamDto;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.*;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBookingService implements AdminBookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    @Override
    public Optional<ReadBookingDto> findByUid(UUID uid) {
        return bookingRepository.findByUid(uid)
            .map(bookingMapper::toReadDto);}

    @Override
    public AdminListBookingResponseDto listBooking(AdminListBookingParamDto paramDto, Pageable pageable) {
        var bookingList = bookingRepository.findAllBookings(paramDto, pageable);
        Stream<ReadBookingDto> dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto);

        var dataPage = RestUtils.getPageEntity(dtoList.toList(), pageable);
        return AdminListBookingResponseDto.builder()
                .bookings(RestUtils.toPageResponse(dataPage))
                .build();
    }

    @Override
    public Optional<UUID> update(UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.getTalent().updateBookingInfo(uid, newBookingData);

        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
    }
}
