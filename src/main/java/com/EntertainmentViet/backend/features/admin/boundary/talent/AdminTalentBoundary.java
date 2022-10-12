package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentDto;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.DoubleStream;

public interface AdminTalentBoundary {
  Optional<ReadAdminTalentDto> findByUid(UUID adminUid, UUID uid);

  Optional<ReadAdminTalentDto> update(UpdateAdminTalentDto updateAdminTalentDto, UUID uid);
}
