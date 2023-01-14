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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminBookingService implements AdminBookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final ConfigBoundary configService;

    @Override
    public Optional<ReadBookingDto> findByUid(UUID uid) {
        return bookingRepository.findByUid(uid)
            .map(bookingMapper::toReadDto);
    }

    @Override
    public AdminListBookingResponseDto listBooking(AdminListBookingParamDto paramDto, Pageable pageable) {
        var bookingList = bookingRepository.findAll(paramDto, pageable);
        var dtoList = bookingList.stream()
            .filter(booking -> booking.getStatus().equals(BookingStatus.FINISHED) && booking.getJobDetail().getPrice().checkIfFixedPrice())
            .map(bookingMapper::toReadDto)
            .toList();

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);

        var unpaidSum = FinanceLogic.calculateUnpaidSum(bookingList);
        var financeReport = FinanceLogic.generateOrganizerBookingReport(bookingList, configService.getFinance().orElse(null), false);
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

        var newBookingData = bookingMapper.fromAdminUpdateDtoToModel(updateBookingDto);
        updatingBooking.getTalent().updateBookingInfo(uid, newBookingData);

        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
    }
}
