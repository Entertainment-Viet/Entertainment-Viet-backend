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

    @Override
    @Transactional
    public Optional<UUID> createForOrganizer(UUID organizerUid, CreateBookingDto createBookingDto) {
        Booking booking = bookingMapper.fromCreateDtoToModel(createBookingDto);
        booking.setStatus(BookingStatus.TALENT_PENDING);

        if (!isBookingValid(booking, createBookingDto.getOrganizerId(), createBookingDto.getTalentId())) {
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

        if (!isBookingValid(booking, createBookingDto.getOrganizerId(), createBookingDto.getTalentId())) {
            return Optional.empty();
        }

        if (!talentUid.equals(booking.getTalent().getUid())) {
            log.warn(String.format("Inconsistent input when creating a booking for talent '%s'", talentUid));
            return Optional.empty();
        }

        return Optional.ofNullable(bookingRepository.save(booking).getUid());
    }

    @Override
    public Optional<UUID> updateFromOrganizer(UUID organizerUid, UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!organizerUid.equals(updatingBooking.getOrganizer().getUid())) {
            log.warn(String.format("Can not find booking with id '%s' belong to organizer with id '%s'", uid, organizerUid));
            return Optional.empty();
        }

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.getOrganizer().updateBookingInfo(uid, newBookingData);

        return Optional.ofNullable(bookingRepository.save(updatingBooking).getUid());
    }

    @Override
    public Optional<UUID> updateFromTalent(UUID talentUid, UUID uid, UpdateBookingDto updateBookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (bookingOptional.isEmpty()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!talentUid.equals(updatingBooking.getTalent().getUid())) {
            log.warn(String.format("Can not find booking with id '%s' belong to talent with id '%s'", uid, talentUid));
            return Optional.empty();
        }

        var newBookingData = bookingMapper.fromUpdateDtoToModel(updateBookingDto);
        updatingBooking.getTalent().updateBookingInfo(uid, newBookingData);

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
