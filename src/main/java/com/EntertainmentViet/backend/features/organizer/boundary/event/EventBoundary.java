package com.EntertainmentViet.backend.features.organizer.boundary.event;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.UpdateEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventBoundary {

  CustomPage<ReadEventDto> findAll(ListEventParamDto paramDto, Pageable pageable);

  Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable);

  Optional<ReadEventDto> findByUid(UUID uid);

  Optional<ReadEventDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid);

  Optional<UUID> create(CreateEventDto createEventDto, UUID organizerUid);

  Optional<UUID> update(UpdateEventDto updateEventDto, UUID organizerUid, UUID uid);

  boolean delete(UUID uid, UUID organizerUid);
}
