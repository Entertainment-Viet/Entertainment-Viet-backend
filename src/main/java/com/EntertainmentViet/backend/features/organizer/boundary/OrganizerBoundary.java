package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;

public interface OrganizerBoundary {

  OrganizerDto findByUid(Long uid);

  Long create(OrganizerDto organizerDto);
}
