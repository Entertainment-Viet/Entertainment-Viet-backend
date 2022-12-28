package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AdminTalentBoundary {
  Optional<ReadAdminTalentDto> findByUid(UUID adminUid, UUID uid);

  Optional<UUID> update(UpdateAdminTalentDto updateAdminTalentDto, UUID uid);

  boolean delete(UUID uid);

  CustomPage<ReadAdminTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable);

  boolean disapprove(UUID uid);
}
