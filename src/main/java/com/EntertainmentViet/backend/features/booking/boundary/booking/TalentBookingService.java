package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.businessLogic.FinanceLogic;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.*;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.finance.boundary.FinanceCalculationBoundary;
import com.EntertainmentViet.backend.features.notification.boundary.BookingNotifyBoundary;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TalentBookingService implements TalentBookingBoundary {

    private final TalentRepository talentRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final FinanceCalculationBoundary financeCalculationService;

    private final BookingNotifyBoundary bookingNotifyService;

    @Override
    public ListBookingResponseDto listBooking(boolean isOwnerUser, UUID talentId, ListTalentBookingParamDto paramDto,
            Pageable pageable) {
        var bookingList = bookingRepository.findByTalentUid(talentId, paramDto, pageable);
        var dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
            .toList();

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);
        var unpaidSum = FinanceLogic.calculateUnpaidSum(bookingList);
        var financeReport = financeCalculationService.exportTalentBookingReport(bookingList,false);
        return ListBookingResponseDto.builder()
            .unpaidSum(unpaidSum)
            .price(financeReport.getPrice())
            .fee(financeReport.getFee())
            .tax(financeReport.getTax())
            .total(financeReport.getTotal())
            .bookings(RestUtils.toPageResponse(dataPage))
            .build();
    }

    @Override
    @Transactional
    public Optional<List<UUID>> create(UUID talentUid, CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.fromCreateDtoToModel(createBookingDto);
        booking.setStatus(BookingStatus.ORGANIZER_PENDING);

        if (!EntityValidationUtils.isBookingValid(booking, createBookingDto.getOrganizerId(), createBookingDto.getTalentId())) {
            return Optional.empty();
        }
        if (!talentUid.equals(booking.getTalent().getUid())) {
            throw new InvalidInputException(String.format("Inconsistent input when creating a booking for talent '%s'", talentUid));
        }

        if (createBookingDto.getRepeatPattern() == null) {
            bookingNotifyService.sendCreateNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking);
            return Optional.of(List.of(bookingRepository.save(booking).getUid()));
        } else {
            List<UUID> createdUid = new ArrayList<>();
            Duration performanceDuration = Duration.between(createBookingDto.getJobDetail().getPerformanceEndTime(), createBookingDto.getJobDetail().getPerformanceStartTime());

            var occurrences = createBookingDto.getRepeatPattern().getAllOccurrence();
            for (var occur : occurrences) {
                Booking repeatBooking = booking.clone();
                repeatBooking.getJobDetail().setPerformanceStartTime(occur);
                repeatBooking.getJobDetail().setPerformanceEndTime(occur.plus(performanceDuration));

                createdUid.add(bookingRepository.save(repeatBooking).getUid());
            }
            bookingNotifyService.sendCreateRepeatNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking, createBookingDto.getRepeatPattern());
            if (createdUid.isEmpty()) {
                log.warn("There is no occurrence matching repeatPattern");
                return Optional.empty();
            } else {
                return Optional.of(createdUid);
            }
        }
    }

    @Override
    public Optional<UUID> update(UUID talentUid, UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!talentUid.equals(updatingBooking.getTalent().getUid())) {
            throw new InvalidInputException(String.format("Can not find booking with id '%s' belong to talent with id '%s'", uid, talentUid));
        }

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.getTalent().updateBookingInfo(uid, newBookingData);

        bookingNotifyService.sendUpdateNotification(updatingBooking.getOrganizer().getUid(), updatingBooking.getOrganizer().getDisplayName(), updatingBooking);
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

        bookingNotifyService.sendAcceptNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking);
        bookingNotifyService.sendAcceptNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking);

        Talent talent = booking.getTalent();
        talent.acceptBooking(bookingId);
        talentRepository.save(talent);
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

        bookingNotifyService.sendRejectNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking);
        bookingNotifyService.sendRejectNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking);

        Talent talent = booking.getTalent();
        talent.rejectBooking(bookingId);
        talentRepository.save(talent);
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

        bookingNotifyService.sendFinishNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking);

        Talent talent = booking.getTalent();
        talent.finishBooking(bookingId);
        talentRepository.save(talent);
        return true;
    }
}
