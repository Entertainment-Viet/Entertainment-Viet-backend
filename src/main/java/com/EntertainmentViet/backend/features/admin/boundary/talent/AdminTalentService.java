package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.talent.PriorityScore;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.talent.AdminTalentMapper;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.TalentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminTalentService implements AdminTalentBoundary {

  private final TalentRepository talentRepository;

  private final AdminTalentMapper adminTalentMapper;

  private final TalentMapper talentMapper;

  @Override
  public Optional<ReadTalentDto> findByUid(UUID adminUid, UUID uid) {
    return talentRepository.findByUid(uid).map(talentMapper::toDto);
  }

  @Override
  public Optional<UUID> update(UpdateAdminTalentDto updateAdminTalentDto, UUID uid) {
    return talentRepository.findByUid(uid)
        .map(talent -> talent.updateInfoByAdmin(adminTalentMapper.toModel(updateAdminTalentDto)))
        .map(talentRepository::save)
        .map(Identifiable::getUid);
  }

  @Override
  public boolean delete(UUID uid) {
    var talent = talentRepository.findByUid(uid).orElse(null);
    if (talent == null)
      return false;
    talent.archive();
    talentRepository.save(talent);
    return true;
  }

  @Override
  public CustomPage<ReadTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        talentRepository.findAll(paramDto, pageable)
            .map(talentMapper::toDto));

    if (talentRepository.findAll(paramDto, pageable.next()).hasContent()) {
      dataPage.getPaging().setLast(false);
    }

    return dataPage;
  }

  @Override
  public boolean disapprove(UUID uid) {
    var talent = talentRepository.findByUid(uid).orElse(null);
    if (talent == null)
      return false;

    talent.setUserState(UserState.UNVERIFIED);

    // disapprove all priority scores
    var pss = talent.getPriorityScores();
    for (PriorityScore ps : pss) {
        ps.setApproved(Boolean.FALSE);
    }
    talentRepository.save(talent);
    return true;
  }
}
