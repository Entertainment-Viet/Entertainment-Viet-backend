package com.EntertainmentViet.backend.features.admin.boundary.bookings;

import com.EntertainmentViet.backend.domain.businessLogic.FinanceLogic;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingParamDto;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminUpdateBookingDto;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.config.boundary.ConfigBoundary;
import com.EntertainmentViet.backend.features.notification.boundary.BookingNotifyBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBookingService implements AdminBookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final ConfigBoundary configService;

    private final BookingNotifyBoundary bookingNotifyService;

    @Override
    public Optional<ReadBookingDto> findByUid(UUID uid) {
        return bookingRepository.findByUid(uid)
            .map(bookingMapper::toReadDto);
    }

    @Override
    public AdminListBookingResponseDto listBooking(AdminListBookingParamDto paramDto, Pageable pageable) {
        var bookingList = bookingRepository.findAll(paramDto, pageable);
        var dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto)
            .toList();

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);

        var unpaidSum = FinanceLogic.calculateUnpaidSum(bookingList);
        var finishBooking = bookingList.stream()
            .filter(booking -> booking.getStatus().equals(BookingStatus.FINISHED) && booking.getJobDetail().getPrice().checkIfFixedPrice())
            .collect(Collectors.toList());

        var financeReport = FinanceLogic.generateOrganizerBookingReport(finishBooking, configService.getFinance().orElse(null), false);
        return AdminListBookingResponseDto.builder()
            .unpaidSum(unpaidSum)
            .price(financeReport.getPrice())
            .fee(financeReport.getFee())
            .tax(financeReport.getTax())
            .total(financeReport.getTotal())
            .bookings(RestUtils.toPageResponse(dataPage))
            .build();
    }

    @Override
    public Optional<UUID> update(UUID uid, AdminUpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();

        if (updateBookingDto.getIsPaid() != null) {
            updatingBooking.setIsPaid(updateBookingDto.getIsPaid());
        }
        if (updateBookingDto.getFinishProof() != null) {
            updatingBooking.setFinishProof(updateBookingDto.getFinishProof());
        }

        bookingNotifyService.sendUpdateNotification(updatingBooking.getOrganizer().getUid(), updatingBooking.getOrganizer().getDisplayName(), updatingBooking);
        bookingNotifyService.sendUpdateNotification(updatingBooking.getTalent().getUid(), updatingBooking.getTalent().getDisplayName(), updatingBooking);
        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
    }
}
