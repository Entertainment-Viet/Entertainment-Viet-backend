package com.EntertainmentViet.backend.features.booking.boundary.booking;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements BookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    @Override
    public Optional<ReadBookingDto> findByUid(boolean isOwnerUser, UUID ownerUid, UUID uid) {
        return bookingRepository.findByUid(uid)
            .filter(booking -> booking.getTalent().getUid().equals(ownerUid) || booking.getOrganizer().getUid().equals(ownerUid))
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser));
    }
}
