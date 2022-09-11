package com.EntertainmentViet.backend.features.booking.boundary.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dao.category.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
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
            .map(bookingMapper::toReadDto);
    }

    @Override
    @Transactional
    public Optional<UUID> createForOrganizer(UUID organizerUid, CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.fromCreateDtoToModel(createBookingDto);
        booking.setStatus(BookingStatus.TALENT_PENDING);

        if (!isBookingValid(booking, createBookingDto.getOrganizerUid(), createBookingDto.getTalentUid())) {
            return Optional.empty();
        }

        if (!organizerUid.equals(booking.getOrganizer().getUid())) {
            log.warn(String.format("Inconsistent input when creating a booking for organizer '%s'", organizerUid));
            return Optional.empty();
        }

        return Optional.ofNullable(bookingRepository.save(booking).getUid());
    }

    @Override
    @Transactional
    public Optional<UUID> createForTalent(UUID talentUid, CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.fromCreateDtoToModel(createBookingDto);
        booking.setStatus(BookingStatus.ORGANIZER_PENDING);

        if (!isBookingValid(booking, createBookingDto.getOrganizerUid(), createBookingDto.getTalentUid())) {
            return Optional.empty();
        }

        if (!talentUid.equals(booking.getOrganizer().getUid())) {
            log.warn(String.format("Inconsistent input when creating a booking for organizer '%s'", talentUid));
            return Optional.empty();
        }

        return Optional.ofNullable(bookingRepository.save(booking).getUid());
    }

    @Override
    public Optional<UUID> update(UUID ownerUid, UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (!bookingOptional.isPresent()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!ownerUid.equals(updatingBooking.getTalent().getUid()) && !ownerUid.equals(updatingBooking.getOrganizer().getUid())) {
            log.warn(String.format("Can not find booking with id '%s' belong to organizer/talent with id '%s'", uid, ownerUid));
            return Optional.empty();
        }

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.updateInfo(newBookingData);

        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
    }

    private boolean isBookingValid(Booking booking, UUID organizerUid, UUID talentUid) {
        if (booking.getOrganizer() == null) {
            log.warn(String.format("Can not find organizer with id '%s' to creating a booking", organizerUid));
            return false;
        }

        if (booking.getTalent() == null) {
            log.warn(String.format("Can not find talent with id '%s' to creating a booking", talentUid));
            return false;
        }

        if (booking.getJobDetail() == null) {
            log.warn("Provided jobDetail for creating booking is invalid");
            return false;
        }
        return true;
    }
}
