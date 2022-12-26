package com.EntertainmentViet.backend.features.organizer.boundary.organizer;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.CreatedOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerKycInfoDto;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface OrganizerBoundary {

  Optional<ReadOrganizerDto> findByUid(UUID uid, boolean isOwnerUser);

  Optional<UUID> create(CreatedOrganizerDto createOrganizerDto, UUID uid);

  Optional<UUID> update(UpdateOrganizerDto updateOrganizerDto, UUID uid);

  Optional<UUID> updateKyc(UpdateOrganizerKycInfoDto kycInfoDto, UUID uid);

  boolean delete(UUID uid);

  boolean sendVerifyRequest(UUID uid);

  boolean verify(UUID uid);

  CustomPage<ReadOrganizerDto> findAll(Pageable pageable);

  boolean approve(UUID uid);

  boolean disapprove(UUID uid);
}
