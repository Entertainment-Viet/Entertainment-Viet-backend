package com.EntertainmentViet.backend.features.organizer.boundary.organizer;

import com.EntertainmentViet.backend.features.organizer.dto.organizer.CreatedOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerKycInfoDto;

import java.util.Optional;
import java.util.UUID;

public interface OrganizerBoundary {

  Optional<ReadOrganizerDto> findByUid(UUID uid);

  Optional<UUID> create(CreatedOrganizerDto createOrganizerDto, UUID uid);

  Optional<UUID> update(UpdateOrganizerDto updateOrganizerDto, UUID uid);

  Optional<UUID> updateKyc(UpdateOrganizerKycInfoDto kycInfoDto, UUID uid);

  boolean sendVerifyRequest(UUID uid);

  boolean verify(UUID uid);
}
