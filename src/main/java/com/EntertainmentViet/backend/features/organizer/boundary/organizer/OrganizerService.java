package com.EntertainmentViet.backend.features.organizer.boundary.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerService implements OrganizerBoundary {

  private final OrganizerRepository organizerRepository;

  private final OrganizerMapper organizerMapper;

  @Override
  public Optional<ReadOrganizerDto> findByUid(UUID id, boolean isOwnerUser) {
    return organizerRepository.findByUid(id)
        .map(organizerMapper::toDto)
        .map(dto -> organizerMapper.checkPermission(dto, isOwnerUser));
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
  public boolean delete(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);
    if (organizer == null)
      return false;
    for (Event event : organizer.getEvents()) {
      event.setArchived(Boolean.TRUE);
    }
    for (JobOffer jobOffer : organizer.getJobOffers()) {
      jobOffer.setArchived(Boolean.TRUE);
    }
    organizerRepository.save(organizer);
    return true;
  }

  @Override
  public boolean sendVerifyRequest(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);

    if (!EntityValidationUtils.isOrganizerWithUid(organizer, uid)) {
      return false;
    }

    if (!organizer.sendVerifyRequest()) {
      return false;
    }
    organizerRepository.save(organizer);
    return true;
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

  @Override
  public CustomPage<ReadOrganizerDto> findAll(Pageable pageable) {
    var dataPage = RestUtils.toLazyLoadPageResponse(
        organizerRepository.findAll(pageable)
            .map(organizerMapper::toDto));

    if (organizerRepository.findAll(pageable.next()).hasContent()) {
      dataPage.getPaging().setLast(false);
    }

    return dataPage;
  }

  @Override
  public boolean approve(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);
    if (organizer == null)
      return false;
    
    organizer.setUserState(UserState.VERIFIED);
    organizerRepository.save(organizer);
    return true;
  }

  @Override
  public boolean disapprove(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);
    if (organizer == null)
      return false;
    
    organizer.setUserState(UserState.UNVERIFIED);
    organizerRepository.save(organizer);
    return true;
  }

}
