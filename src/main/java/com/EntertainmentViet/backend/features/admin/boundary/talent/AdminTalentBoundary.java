package com.EntertainmentViet.backend.features.admin.boundary.talent;

import java.util.Optional;
import java.util.UUID;

import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;

public interface AdminTalentBoundary {
  Optional<ReadAdminTalentDto> findByUid(UUID adminUid, UUID uid);

  Optional<UUID> update(UpdateAdminTalentDto updateAdminTalentDto, UUID uid);

  boolean approve(UUID uid);

  boolean disapprove(UUID uid);
}
