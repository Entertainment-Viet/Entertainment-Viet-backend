package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;

import java.util.UUID;

public interface OrganizerBoundary {

  OrganizerDto findByUid(UUID uid);

  OrganizerDto create(OrganizerDto organizerDto);
}
