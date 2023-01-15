package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.businessLogic.FinanceLogic;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.DetailBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.config.boundary.ConfigBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements BookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final ConfigBoundary configService;

    @Override
    public Optional<DetailBookingResponseDto> findByUidForOrganizer(boolean isOwnerUser, UUID organizerUid, UUID uid) {
        var fetchedBooking = bookingRepository.findByUid(uid);
        var readDto = fetchedBooking
            .filter(booking -> booking.getOrganizer().getUid().equals(organizerUid))
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
            .orElse(ReadBookingDto.builder().build());

        var financeReport = FinanceLogic.generateOrganizerBookingReport(Arrays.asList(fetchedBooking.get()), configService.getFinance().orElse(null), true);
        return Optional.of(DetailBookingResponseDto.builder()
            .readBookingDto(readDto)
            .financeReport(financeReport)
            .build());
    }

    @Override
    public Optional<DetailBookingResponseDto> findByUidForTalent(boolean isOwnerUser, UUID talentUid, UUID uid) {
        var fetchedBooking = bookingRepository.findByUid(uid);
        var readDto = fetchedBooking
            .filter(booking -> booking.getTalent().getUid().equals(talentUid))
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
            .orElse(ReadBookingDto.builder().build());

        var financeReport = FinanceLogic.generateTalentBookingReport(Arrays.asList(fetchedBooking.get()), configService.getFinance().orElse(null), true);
        return Optional.of(DetailBookingResponseDto.builder()
            .readBookingDto(readDto)
            .financeReport(financeReport)
            .build());
    }
}
