package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadTalentDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AdminTalentBoundary {
  Optional<ReadTalentDto> findByUid(UUID adminUid, UUID uid);

  Optional<UUID> update(UpdateAdminTalentDto updateAdminTalentDto, UUID uid);

  boolean delete(UUID uid);

  CustomPage<ReadTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable);

  boolean disapprove(UUID uid);
}
