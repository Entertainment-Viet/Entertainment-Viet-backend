package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventOpenPositionDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventPositionParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventOpenPositionDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.UpdateEventOpenPositionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventOpenPositionBoundary {

  Page<ReadEventOpenPositionDto> findByOrganizerUidAndEventUid(UUID organizerUid, UUID eventUid, ListEventPositionParamDto paramDto, Pageable pageable);

  Optional<UUID> createInEvent(UUID organizerUid, UUID eventUid, CreateEventOpenPositionDto createEventOpenPositionDto);

  Optional<UUID> update(UUID organizerUid, UUID eventUid, UUID uid, UpdateEventOpenPositionDto updateEventOpenPositionDto);

  boolean delete(UUID organizerUid, UUID eventUid, UUID uid);

  Optional<ReadEventOpenPositionDto> findByUid(UUID organizerUid, UUID eventUid, UUID uid);
}
