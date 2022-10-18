package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements BookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    @Override
    public Optional<ReadBookingDto> findByUid(UUID ownerUid, UUID uid) {
        return bookingRepository.findByUid(uid)
            .filter(booking -> booking.getTalent().getUid().equals(ownerUid) || booking.getOrganizer().getUid().equals(ownerUid))
            .map(bookingMapper::toReadDto)
            .map(bookingMapper::checkPermission);
    }
}
