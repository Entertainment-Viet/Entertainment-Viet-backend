package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.features.organizer.dto.event.CreateEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.UpdateEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.DoubleStream;

public interface EventBoundary {

  Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable);

  Optional<ReadEventDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid);

  Optional<UUID> create(CreateEventDto createEventDto, UUID organizerUid);

  Optional<UUID> update(UpdateEventDto updateEventDto, UUID organizerUid, UUID uid);

  boolean delete(UUID uid, UUID organizerUid);
}
