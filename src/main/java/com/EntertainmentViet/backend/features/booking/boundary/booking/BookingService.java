package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.values.FinanceReport;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.DetailBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.finance.boundary.FinanceCalculationBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService implements BookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final FinanceCalculationBoundary financeCalculationService;

    @Override
    public Optional<DetailBookingResponseDto> findByUid(boolean isOwnerUser, UUID ownerUid, UUID uid) {
        var fetchedBookingOptional = bookingRepository.findByUid(uid);
        var readDto = fetchedBookingOptional
            .filter(booking -> booking.getTalent().getUid().equals(ownerUid) || booking.getOrganizer().getUid().equals(ownerUid))
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
            .orElse(ReadBookingDto.builder().build());

        if (fetchedBookingOptional.isEmpty()) {
            return Optional.of(DetailBookingResponseDto.builder().build());
        }

        var fetchedBooking = fetchedBookingOptional.get();
        FinanceReport financeReport;
        if (fetchedBooking.getStatus() == BookingStatus.CONFIRMED || fetchedBooking.getStatus() == BookingStatus.FINISHED) {
            financeReport = financeCalculationService.exportOrganizerBookingReport(List.of(fetchedBooking),true);
        } else if (fetchedBooking.getStatus() == BookingStatus.ORGANIZER_PENDING || fetchedBooking.getStatus() == BookingStatus.ORGANIZER_FINISH) {
            financeReport = financeCalculationService.exportOrganizerBookingReport(List.of(fetchedBooking),true);
        } else if (fetchedBooking.getStatus() == BookingStatus.TALENT_PENDING || fetchedBooking.getStatus() == BookingStatus.TALENT_FINISH) {
            financeReport = financeCalculationService.exportTalentBookingReport(List.of(fetchedBooking),true);
        } else {
            financeReport = FinanceReport.builder().build();
        }
        return Optional.of(DetailBookingResponseDto.builder()
            .readBookingDto(readDto)
            .financeReport(financeReport)
            .build());
    }
}
