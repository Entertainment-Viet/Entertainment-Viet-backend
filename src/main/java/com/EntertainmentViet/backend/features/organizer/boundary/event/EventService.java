package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
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
  public CustomPage<ReadEventDto> findAll(ListEventParamDto paramDto, Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        eventRepository.findAll(paramDto, pageable)
            .map(eventMapper::toReadDto)
    );

    if (eventRepository.findAll(paramDto, pageable.next()).hasContent()) {
      dataPage.getPaging().setLast(false);
    }

    return dataPage;
  }

  @Override
  public Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable) {
    var dtoList = eventRepository.findByOrganizerUid(uid, paramDto, pageable).stream()
        .map(eventMapper::toReadDto)
        .collect(Collectors.toList());

    return RestUtils.getPageEntity(dtoList, pageable);
  }

  @Override
  public Optional<ReadEventDto> findByUid(UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }
    return Optional.ofNullable(eventMapper.toReadDto(event));

  }

  @Override
  public Optional<ReadEventDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    return Optional.ofNullable(eventMapper.toReadDto(event));
  }

  @Override
  public Optional<UUID> create(CreateEventDto createEventDto, UUID organizerUid) {
    Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
    Event createEvent = eventMapper.fromCreateDtoToModel(createEventDto);
    createEvent.setOrganizer(organizer);
    createEvent.getEventDetail().setEvent(createEvent);

    return Optional.ofNullable(eventRepository.save(createEvent).getUid());
  }

  @Override
  public Optional<UUID> update(UpdateEventDto updateEventDto, UUID organizerUid, UUID uid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return Optional.empty();
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return Optional.empty();
    }

    Event newEvent = eventMapper.fromUpdateDtoToModel(updateEventDto);
    event.updateInfo(newEvent);
    return Optional.ofNullable(eventRepository.save(event).getUid());
  }

  @Override
  public boolean delete(UUID uid, UUID organizerUid) {
    Event event = eventRepository.findByUid(uid).orElse(null);
    if (!EntityValidationUtils.isEventWithUidExist(event, uid)) {
      return false;
    }

    if (!EntityValidationUtils.isEventBelongToOrganizerWithUid(event, organizerUid)) {
      return false;
    }

    eventRepository.deleteById(event.getId());
    return true;
  }
}
