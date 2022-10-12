package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.features.admin.dto.talent.AdminTalentMapper;
import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.TalentMapper;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminTalentService implements AdminTalentBoundary {

  private final TalentRepository talentRepository;

  private final AdminTalentMapper adminTalentMapper;

  @Override
  public Optional<ReadAdminTalentDto> findByUid(UUID adminUid, UUID uid) {
    return talentRepository.findByUid(uid).map(adminTalentMapper::toAdminDto);
  }

  @Override
  public Optional<ReadAdminTalentDto> update(UpdateAdminTalentDto updateAdminTalentDto, UUID uid) {
    return talentRepository.findByUid(uid)
        .map(talent -> talent.updateInfo(adminTalentMapper.toModel(updateAdminTalentDto)))
        .map(talentRepository::save)
        .map(adminTalentMapper::toAdminDto);
  }
}
