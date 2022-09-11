package com.EntertainmentViet.backend.features.organizer.boundary.joboffer;

import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.UpdateJobOfferDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOfferBoundary {

    List<ReadJobOfferDto> findByOrganizerUid(UUID uid);

    Optional<ReadJobOfferDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid);

    Optional<UUID> create(CreateJobOfferDto createJobOfferDto, UUID organizerUid);

    Optional<UUID> update(UpdateJobOfferDto updateJobOfferDto, UUID organizerUid, UUID uid);

    boolean delete(UUID uid, UUID organizerUid);
}
