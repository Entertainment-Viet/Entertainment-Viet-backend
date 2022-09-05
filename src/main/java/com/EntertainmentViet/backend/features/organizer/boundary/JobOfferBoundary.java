package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dto.JobOfferDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOfferBoundary {

    List<JobOfferDto> findByOrganizerUid(UUID uid);

    Optional<JobOfferDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid);

    Optional<UUID> create(JobOfferDto jobOfferDto, UUID organizerUid);

    Optional<UUID> update(JobOfferDto jobOfferDto, UUID organizerUid, UUID uid);

    boolean delete(UUID uid, UUID organizerUid);
}
