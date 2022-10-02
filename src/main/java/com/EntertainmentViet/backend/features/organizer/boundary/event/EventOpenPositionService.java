package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventOpenPositionRepository;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    if (!isEventWithUidExist(event, eventUid)) {
      return Optional.empty();
    }

    if (!isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    EventOpenPosition eventOpenPosition = eventOpenPositionMapper.fromCreateDtoToModel(createEventOpenPositionDto);
    eventOpenPosition.setEvent(event);
    return Optional.ofNullable(eventOpenPositionRepository.save(eventOpenPosition).getUid());
  }

  @Override
  public Optional<UUID> update(UUID organizerUid, UUID eventUid, UUID uid, UpdateEventOpenPositionDto updateEventOpenPositionDto) {
    EventOpenPosition eventOpenPosition = eventOpenPositionRepository.findByUid(uid).orElse(null);
    if (!isOpenPositionWithUidExist(eventOpenPosition, uid)) {
      return Optional.empty();
    }

    if (!isOpenPositionBelongToEventWithUid(eventOpenPosition, eventUid)) {
      return Optional.empty();
    }

    if (!isEventBelongToOrganizerWithUid(eventOpenPosition.getEvent(), organizerUid)) {
      return Optional.empty();
    }

    EventOpenPosition newEventOpenPosition = eventOpenPositionMapper.fromUpdateDtoToModel(updateEventOpenPositionDto);
    eventOpenPosition.updateInfo(newEventOpenPosition);
    return Optional.ofNullable(eventOpenPositionRepository.save(eventOpenPosition).getUid());
  }

  @Override
  public boolean delete(UUID organizerUid, UUID eventUid, UUID uid) {
    EventOpenPosition eventOpenPosition = eventOpenPositionRepository.findByUid(uid).orElse(null);
    if (!isOpenPositionWithUidExist(eventOpenPosition, uid)) {
      return false;
    }

    if (!isOpenPositionBelongToEventWithUid(eventOpenPosition, eventUid)) {
      return false;
    }

    if (!isEventBelongToOrganizerWithUid(eventOpenPosition.getEvent(), organizerUid)) {
      return false;
    }

    eventOpenPositionRepository.deleteById(eventOpenPosition.getId());
    return true;
  }

  private boolean isOpenPositionWithUidExist(EventOpenPosition eventOpenPosition, UUID uid) {
    if (eventOpenPosition == null) {
      log.warn(String.format("Can not find event open position with id '%s'", uid));
      return false;
    }
    return true;
  }

  private boolean isOpenPositionBelongToEventWithUid(EventOpenPosition eventOpenPosition, UUID eventUid) {
    Event event = eventOpenPosition.getEvent();
    if (!event.getUid().equals(eventUid)) {
      log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", eventOpenPosition.getUid(), eventUid));
      return false;
    }
    return true;
  }

  private boolean isEventWithUidExist(Event event, UUID uid) {
    if (event == null) {
      log.warn(String.format("Can not find event with id '%s'", uid));
      return false;
    }
    return true;
  }

  private boolean isEventBelongToOrganizerWithUid(Event event, UUID organizerUid) {
    Organizer organizer = event.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", event.getUid(), organizerUid));
      return false;
    }
    return true;
  }
}
