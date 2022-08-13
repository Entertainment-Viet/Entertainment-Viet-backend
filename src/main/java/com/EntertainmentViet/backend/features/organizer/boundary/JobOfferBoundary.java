package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.booking.dto.JobOfferDto;

import java.util.List;
import java.util.UUID;

public interface JobOfferBoundary {

    List<JobOfferDto> findByOrganizerUid(UUID uid);

    JobOfferDto findByOrganizerUidAndUid(UUID organizerUid, UUID uid);
}
