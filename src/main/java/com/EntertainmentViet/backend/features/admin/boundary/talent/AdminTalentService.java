package com.EntertainmentViet.backend.features.admin.boundary.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.talent.AdminTalentMapper;
import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
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
    return true;
  }

  @Override
  public boolean approve(UUID uid) {
    var talent = talentRepository.findByUid(uid).orElse(null);
    if (talent == null)
      return false;

    talent.setUserState(UserState.VERIFIED);
    talentRepository.save(talent);
    return true;
  }

  @Override
  public CustomPage<ReadAdminTalentDto> findAll(Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        talentRepository.findAll(pageable)
            .map(adminTalentMapper::toAdminDto));

    if (talentRepository.findAll(pageable.next()).hasContent()) {
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
    talentRepository.save(talent);
    return true;
  }
}
