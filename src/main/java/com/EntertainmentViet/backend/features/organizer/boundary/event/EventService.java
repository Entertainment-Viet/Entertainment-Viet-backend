package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventRepository;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
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
public class EventService implements EventBoundary {

  private final EventRepository eventRepository;

  private final OrganizerRepository organizerRepository;

  private final EventMapper eventMapper;

  @Override
  public Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable) {
    var dtoList = eventRepository.findByOrganizerUid(uid, paramDto, pageable).stream()
        .map(eventMapper::toReadDto)
        .collect(Collectors.toList());

    return RestUtils.getPageEntity(dtoList, pageable);
  }

  @Override
  public Optional<ReadEventDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }

    if (!isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    return Optional.ofNullable(eventMapper.toReadDto(event));
  }

  @Override
  public Optional<UUID> create(CreateEventDto createEventDto, UUID organizerUid) {
    Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
    Event createEvent = eventMapper.fromCreateDtoToModel(createEventDto);
    createEvent.setOrganizer(organizer);

    return Optional.ofNullable(eventRepository.save(createEvent).getUid());
  }

  @Override
  public Optional<UUID> update(UpdateEventDto updateEventDto, UUID organizerUid, UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }

    if (!isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    Event newEvent = eventMapper.fromUpdateDtoToModel(updateEventDto);
    event.updateInfo(newEvent);
    return Optional.ofNullable(eventRepository.save(event).getUid());
  }

  @Override
  public boolean delete(UUID uid, UUID organizerUid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!isEventWithUidExist(event, uid)) {
      return false;
    }

    if (!isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return false;
    }

    eventRepository.deleteById(event.getId());
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
