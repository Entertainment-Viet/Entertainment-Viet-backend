package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventOpenPositionRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreatePositionApplicantDto;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPositionBookingService implements EventPositionBookingBoundary {

  private final EventOpenPositionRepository eventOpenPositionRepository;

  private final TalentRepository talentRepository;

  private final BookingRepository bookingRepository;

  private final BookingMapper bookingMapper;

  private final JobDetailMapper jobDetailMapper;

  @Override
  public Page<ReadBookingDto> findByPositionUid(UUID organizerUid, UUID eventUid, UUID positionUid, ListOrganizerBookingParamDto paramDto, Pageable pageable) {
    EventOpenPosition openPosition = eventOpenPositionRepository.findByUid(positionUid).orElse(null);

    if (!EntityValidationUtils.isOpenPositionWithUidExist(openPosition, positionUid)) {
      return Page.empty();
    }
    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(openPosition, eventUid)) {
      return Page.empty();
    }
    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(openPosition.getEvent(), organizerUid)) {
      return Page.empty();
    }

    var dtoList = openPosition.getApplicants().stream()
        .map(bookingMapper::toReadDto)
        .collect(Collectors.toList());

    return RestUtils.getPageEntity(dtoList, pageable);
  }

  @Override
  @Transactional
  public Optional<UUID> create(UUID organizerUid, UUID eventUid, UUID positionUid, CreatePositionApplicantDto createPositionApplicantDto) {
    EventOpenPosition openPosition = eventOpenPositionRepository.findByUid(positionUid).orElse(null);
    Talent talent = talentRepository.findByUid(createPositionApplicantDto.getTalentId()).orElse(null);

    if (!EntityValidationUtils.isOpenPositionWithUidExist(openPosition, positionUid)) {
      return Optional.empty();
    }
    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(openPosition, eventUid)) {
      return Optional.empty();
    }
    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(openPosition.getEvent(), organizerUid)) {
      return Optional.empty();
    }
    if (!EntityValidationUtils.isTalentWithUid(talent, createPositionApplicantDto.getTalentId())) {
      return Optional.empty();
    }

    Booking createdApplicant = talent.applyToEventPosition(openPosition, createPositionApplicantDto.getSuggestedPrice());
    var newBooking = bookingRepository.save(createdApplicant);

    talentRepository.save(talent);
    return Optional.of(newBooking.getUid());
  }

  @Override
  public boolean acceptBooking(UUID organizerUid, UUID eventUid, UUID positionUid, UUID bookingUid) {
    EventOpenPosition openPosition = eventOpenPositionRepository.findByUid(positionUid).orElse(null);

    if (!EntityValidationUtils.isOpenPositionWithUidExist(openPosition, positionUid)) {
      return false;
    }
    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(openPosition, eventUid)) {
      return false;
    }
    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(openPosition.getEvent(), organizerUid)) {
      return false;
    }

    try {
      openPosition.acceptApplicant(bookingUid);
      eventOpenPositionRepository.save(openPosition);
    } catch (EntityNotFoundException ex) {
      log.warn(ex.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public boolean rejectBooking(UUID organizerUid, UUID eventUid, UUID positionUid, UUID bookingUid) {
    EventOpenPosition openPosition = eventOpenPositionRepository.findByUid(positionUid).orElse(null);

    if (!EntityValidationUtils.isOpenPositionWithUidExist(openPosition, positionUid)) {
      return false;
    }
    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(openPosition, eventUid)) {
      return false;
    }
    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(openPosition.getEvent(), organizerUid)) {
      return false;
    }

    try {
      openPosition.rejectApplicant(bookingUid);
      eventOpenPositionRepository.save(openPosition);
    } catch (EntityNotFoundException ex) {
      log.warn(ex.getMessage());
      return false;
    }
    return true;
  }
}
