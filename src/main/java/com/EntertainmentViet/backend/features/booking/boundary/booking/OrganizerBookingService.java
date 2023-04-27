package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.businessLogic.CronExpressionLogic;
import com.EntertainmentViet.backend.domain.businessLogic.FinanceLogic;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.exception.rest.InconsistentEntityStateException;
import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.*;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.finance.boundary.FinanceCalculationBoundary;
import com.EntertainmentViet.backend.features.notification.boundary.BookingNotifyBoundary;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.boundary.talent.ReviewBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final BookingNotifyBoundary bookingNotifyService;

    private final FinanceCalculationBoundary financeCalculationService;

    @Override
    public ListBookingResponseDto listBooking(boolean isOwnerUser, UUID organizerId, ListOrganizerBookingParamDto paramDto,
            Pageable pageable) {
        var bookingList = bookingRepository.findByOrganizerUid(organizerId, paramDto, pageable);
        var dtoList = bookingList.stream()
            .map(bookingMapper::toReadDto)
            .map(dto -> bookingMapper.checkPermission(dto, isOwnerUser))
            .toList();

        var dataPage = RestUtils.getPageEntity(dtoList, pageable);
        var unpaidSum = FinanceLogic.calculateUnpaidSum(bookingList);
        var financeReport = financeCalculationService.exportOrganizerBookingReport(bookingList, false);
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
    public Optional<List<UUID>> create(UUID organizerUid, CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.fromCreateDtoToModel(createBookingDto);
        booking.setStatus(BookingStatus.TALENT_PENDING);

        if (!EntityValidationUtils.isBookingValid(booking, createBookingDto.getOrganizerId(), createBookingDto.getTalentId())) {
            return Optional.empty();
        }
        if (!organizerUid.equals(booking.getOrganizer().getUid())) {
            throw new InvalidInputException(String.format("Inconsistent input when creating a booking for organizer '%s'", organizerUid));
        }

        if (createBookingDto.getRepeatPattern() == null) {
            bookingNotifyService.sendCreateNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking);
            return Optional.of(List.of(bookingRepository.save(booking).getUid()));
        } else {
            List<UUID> createdUid = CronExpressionLogic.generateBookingList(booking, createBookingDto.getRepeatPattern())
                .stream()
                .map(bookingRepository::save)
                .map(Identifiable::getUid)
                .collect(Collectors.toList());
            bookingNotifyService.sendCreateRepeatNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking, createBookingDto.getRepeatPattern());
            if (createdUid.isEmpty()) {
                throw new InvalidInputException("There is no occurrence matching repeatPattern");
            } else {
                return Optional.of(createdUid);
            }
        }
    }

    @Override
    public Optional<UUID> update(UUID organizerUid, UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!organizerUid.equals(updatingBooking.getOrganizer().getUid())) {
            throw new InvalidInputException(String.format("Can not find booking with id '%s' belong to organizer with id '%s'", uid, organizerUid));
        }

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.getOrganizer().updateBookingInfo(uid, newBookingData);

        bookingNotifyService.sendUpdateNotification(updatingBooking.getTalent().getUid(), updatingBooking.getTalent().getDisplayName(), updatingBooking);
        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
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

        bookingNotifyService.sendAcceptNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking);
        bookingNotifyService.sendAcceptNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking);

        Organizer organizer = booking.getOrganizer();
        organizer.acceptBooking(bookingId);
        organizerRepository.save(organizer);
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

        bookingNotifyService.sendRejectNotification(booking.getOrganizer().getUid(), booking.getOrganizer().getDisplayName(), booking);
        bookingNotifyService.sendRejectNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking);

        Organizer organizer = booking.getOrganizer();
        organizer.rejectBooking(bookingId);
        organizerRepository.save(organizer);
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

        bookingNotifyService.sendFinishNotification(booking.getTalent().getUid(), booking.getTalent().getDisplayName(), booking);

        Organizer organizer = booking.getOrganizer();
        organizer.finishBooking(bookingId);
        organizerRepository.save(organizer);
        return true;
    }

    @Transactional(rollbackFor = {InconsistentEntityStateException.class})
    public Optional<UUID> finishBooingAndReview(CreateReviewDto reviewDto, UUID organizerUid, UUID bookingUid) {
        Optional<UUID> result;
        if (!finishBooking(organizerUid, bookingUid)) {
            result = Optional.empty();
        } else {
            result = reviewService.addReviewToBooking(reviewDto, bookingUid);
        }
        if (result.isEmpty()) {
            log.error("Rollback database operation");
            throw new InconsistentEntityStateException();
        }
        return  result;
    }
}
