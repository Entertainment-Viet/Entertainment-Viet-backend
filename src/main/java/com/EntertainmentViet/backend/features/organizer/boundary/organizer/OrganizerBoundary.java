package com.EntertainmentViet.backend.features.organizer.boundary.organizer;

import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;

import java.util.Optional;
import java.util.UUID;

public interface OrganizerBoundary {

  Optional<ReadOrganizerDto> findByUid(UUID uid);

  Optional<UUID> create(UpdateOrganizerDto updateOrganizerDto, UUID uid);

  Optional<UUID> update(UpdateOrganizerDto updateOrganizerDto, UUID uid);

  boolean verify(UUID uid);
}
