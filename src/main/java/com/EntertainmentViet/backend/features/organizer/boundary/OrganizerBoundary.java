package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;

public interface OrganizerBoundary {

  OrganizerDto findById(Long id);

  Long create(OrganizerDto organizerDto);
}
