package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.features.booking.dao.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements BookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    @Override
    public BookingDto findByUid(UUID uid) {
        return bookingMapper.toDto(bookingRepository.findByUid(uid).orElse(null));
    }
}
