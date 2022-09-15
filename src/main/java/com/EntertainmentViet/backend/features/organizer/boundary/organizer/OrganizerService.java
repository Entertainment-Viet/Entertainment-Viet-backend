package com.EntertainmentViet.backend.features.organizer.boundary.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.OrganizerMapper;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;
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
    return organizerRepository.findByUid(id).map(organizerMapper::toDto);
  }

  @Override
  public Optional<UUID> create(UpdateOrganizerDto createOrganizerDto, UUID uid) {
    var newOrganizer = organizerMapper.toModel(createOrganizerDto);
    newOrganizer.setUid(uid);
    newOrganizer.setUserState(UserState.GUEST);

    return Optional.ofNullable(organizerRepository.save(newOrganizer).getUid());
  }

  @Override
  public Optional<UUID> update(UpdateOrganizerDto updateOrganizerDto, UUID uid) {
    return organizerRepository.findByUid(uid)
        .map(organizer -> organizer.updateInfo(organizerMapper.toModel(updateOrganizerDto)))
        .map(organizerRepository::save)
        .map(Identifiable::getUid);
  }

  @Override
  @Transactional
  public boolean verify(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);

    if (organizer == null) {
      log.warn(String.format("Can not find organizer with id '%s'", uid));
      return false;
    }
    if (!organizer.verifyAccount()) {
      return false;
    }
    organizerRepository.save(organizer);
    return true;
  }
}
