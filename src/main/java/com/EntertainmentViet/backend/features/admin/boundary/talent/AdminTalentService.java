package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.PriorityScore;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.talent.AdminTalentMapper;
import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;

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

  @Override
  public Optional<ReadAdminTalentDto> findByUid(UUID adminUid, UUID uid) {
    return talentRepository.findByUid(uid).map(adminTalentMapper::toAdminDto);
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
    talent.setArchived(Boolean.TRUE);
    var packages_it = talent.getPackages().iterator();
    while (packages_it.hasNext()) {
      Package pk = packages_it.next();
      pk.setArchived(Boolean.TRUE);
    }
    talentRepository.save(talent);
    return true;
  }

  @Override
  public boolean approve(UUID uid) {
    var talent = talentRepository.findByUid(uid).orElse(null);
    if (talent == null)
      return false;

    talent.setUserState(UserState.VERIFIED);

    // approve priority score which are not yet set
    var pss = talent.getPriorityScores();
    for (PriorityScore ps : pss) {
      if (ps.getApproved() == Boolean.FALSE) {
        ps.setApproved(Boolean.TRUE);
      }
    }
    talentRepository.save(talent);
    return true;
  }

  @Override
  public CustomPage<ReadAdminTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        talentRepository.findAll(pageable)
            .map(adminTalentMapper::toAdminDto));

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

    // disapprove priority score which are already set
    var pss = talent.getPriorityScores();
    for (PriorityScore ps : pss) {
        ps.setApproved(Boolean.FALSE);
    }
    talentRepository.save(talent);
    return true;
  }
}
