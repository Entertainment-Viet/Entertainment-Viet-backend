package com.EntertainmentViet.backend.features.organizer.boundary.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerService implements OrganizerBoundary {

  private final OrganizerRepository organizerRepository;

  private final OrganizerMapper organizerMapper;

  @Override
  public Optional<ReadOrganizerDto> findByUid(UUID id) {
    return organizerRepository.findByUid(id)
        .map(organizerMapper::toDto)
        .map(organizerMapper::checkPermission);
  }

  @Override
  public Optional<UUID> create(CreatedOrganizerDto createOrganizerDto, UUID uid) {
    var createdOrganizer = organizerMapper.fromCreateDtoToModel(createOrganizerDto);
    createdOrganizer.setUid(uid);
    createdOrganizer.setUserState(UserState.GUEST);
    createdOrganizer.getOrganizerDetail().setOrganizer(createdOrganizer);

    return Optional.ofNullable(organizerRepository.save(createdOrganizer).getUid());
  }

  @Override
  public Optional<UUID> update(UpdateOrganizerDto updateOrganizerDto, UUID uid) {
    return organizerRepository.findByUid(uid)
        .map(organizer -> organizer.updateInfo(organizerMapper.fromUpdateDtoToModel(updateOrganizerDto)))
        .map(organizerRepository::save)
        .map(Identifiable::getUid);
  }

  @Override
  public Optional<UUID> updateKyc(UpdateOrganizerKycInfoDto kycInfoDto, UUID uid) {
    return organizerRepository.findByUid(uid)
        .map(organizer -> organizer.requestKycInfoChange(organizerMapper.fromKycDtoToModel(kycInfoDto)))
        .map(organizerRepository::save)
        .map(Identifiable::getUid);
  }

  @Override
  @Transactional
  public boolean verify(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);

    if (!EntityValidationUtils.isOrganizerWithUid(organizer, uid)) {
      return false;
    }

    if (!organizer.verifyAccount()) {
      return false;
    }
    organizerRepository.save(organizer);
    return true;
  }
}
