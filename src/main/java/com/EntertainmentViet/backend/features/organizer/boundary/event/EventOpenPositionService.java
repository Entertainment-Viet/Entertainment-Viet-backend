package com.EntertainmentViet.backend.features.organizer.boundary.event;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventOpenPositionRepository;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventOpenPositionDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.EventOpenPositionMapper;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventPositionParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventOpenPositionDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.UpdateEventOpenPositionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventOpenPositionService implements EventOpenPositionBoundary {

  private final EventOpenPositionRepository eventOpenPositionRepository;

  private final EventRepository eventRepository;

  private final EventOpenPositionMapper eventOpenPositionMapper;

  @Override
  public Page<ReadEventOpenPositionDto> findByOrganizerUidAndEventUid(UUID organizerUid, UUID eventUid, ListEventPositionParamDto paramDto, Pageable pageable) {
    var dtoList = eventOpenPositionRepository.findByOrganizerUidAndEventUid(organizerUid, eventUid, paramDto, pageable).stream()
        .map(eventOpenPositionMapper::toDto)
        .collect(Collectors.toList());

    return RestUtils.getPageEntity(dtoList, pageable);
  }

  @Override
  public Optional<UUID> createInEvent(UUID organizerUid, UUID eventUid, CreateEventOpenPositionDto createEventOpenPositionDto) {
    Event event = eventRepository.findByUid(eventUid).orElse(null);

    if (!EntityValidationUtils.isEventWithUidExist(event, eventUid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    EventOpenPosition eventOpenPosition = eventOpenPositionMapper.fromCreateDtoToModel(createEventOpenPositionDto);
    eventOpenPosition.setEvent(event);
    eventOpenPosition.getJobOffer().getJobDetail().setLocation(event.getEventDetail().getOccurrenceAddress());
    eventOpenPosition.getJobOffer().setOrganizer(eventOpenPosition.getEvent().getOrganizer());
    return Optional.ofNullable(eventOpenPositionRepository.save(eventOpenPosition).getUid());
  }

  @Override
  public Optional<UUID> update(UUID organizerUid, UUID eventUid, UUID uid, UpdateEventOpenPositionDto updateEventOpenPositionDto) {
    EventOpenPosition eventOpenPosition = eventOpenPositionRepository.findByUid(uid).orElse(null);

    if (!EntityValidationUtils.isOpenPositionWithUidExist(eventOpenPosition, uid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(eventOpenPosition, eventUid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(eventOpenPosition.getEvent(), organizerUid)) {
      return Optional.empty();
    }

    EventOpenPosition newEventOpenPosition = eventOpenPositionMapper.fromUpdateDtoToModel(updateEventOpenPositionDto);
    newEventOpenPosition.getJobOffer().getJobDetail().setLocation(eventOpenPosition.getJobOffer().getJobDetail().getLocation());
    eventOpenPosition.updateInfo(newEventOpenPosition);
    return Optional.ofNullable(eventOpenPositionRepository.save(eventOpenPosition).getUid());
  }

  @Override
  public boolean delete(UUID organizerUid, UUID eventUid, UUID uid) {
    EventOpenPosition eventOpenPosition = eventOpenPositionRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isOpenPositionWithUidExist(eventOpenPosition, uid)) {
      return false;
    }

    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(eventOpenPosition, eventUid)) {
      return false;
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(eventOpenPosition.getEvent(), organizerUid)) {
      return false;
    }

    eventOpenPosition.setArchived(Boolean.TRUE);
    eventOpenPositionRepository.save(eventOpenPosition);
    return true;
  }

  @Override
  public Optional<ReadEventOpenPositionDto> findByUid(UUID organizerUid, UUID eventUid, UUID uid) {
    EventOpenPosition eventOpenPosition = eventOpenPositionRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isOpenPositionWithUidExist(eventOpenPosition, uid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isOpenPositionBelongToEventWithUid(eventOpenPosition, eventUid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(eventOpenPosition.getEvent(), organizerUid)) {
      return Optional.empty();
    }

    return Optional.ofNullable(eventOpenPositionMapper.toDto(eventOpenPosition));
  }
}
