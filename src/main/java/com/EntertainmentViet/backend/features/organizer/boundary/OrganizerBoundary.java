package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;

import java.util.Optional;
import java.util.UUID;

public interface OrganizerBoundary {

  Optional<OrganizerDto> findByUid(UUID uid);

  Optional<UUID> create(OrganizerDto organizerDto);

  Optional<UUID> update(OrganizerDto organizerDto, UUID uid);
}
