package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizerService implements OrganizerBoundary {

  private final OrganizerRepository organizerRepository;

  private final OrganizerMapper organizerMapper;

  @Override
  public Optional<OrganizerDto> findByUid(UUID id) {
    return organizerRepository.findByUid(id).map(organizerMapper::toDto);
  }

  @Override
  public Optional<UUID> create(OrganizerDto organizerDto) {
    // TODO add check not exist username
    var newOrganizer = organizerMapper.toModel(organizerDto);
    newOrganizer.setUid(null);
    newOrganizer.setId(null);
    newOrganizer.setBookings(Collections.emptyList());
    newOrganizer.setEvents(Collections.emptyList());
    newOrganizer.setFeedbacks(Collections.emptyList());
    newOrganizer.setJobOffers(Collections.emptyList());
    newOrganizer.setShoppingCart(Collections.emptySet());
    newOrganizer.setUserState(UserState.GUEST);

    return Optional.ofNullable(organizerRepository.save(newOrganizer).getUid());
  }

  @Override
  public Optional<UUID> update(OrganizerDto organizerDto, UUID uid) {
    return organizerRepository.findByUid(uid)
        .map(organizer -> updateOrganizerInfo(organizer, organizerMapper.toModel(organizerDto)))
        .map(organizerRepository::save)
        .map(Identifiable::getUid);
  }

  @Override
  public boolean verify(UUID uid) {
    var organizer = organizerRepository.findByUid(uid).orElse(null);

    if (organizer == null) {
      log.warn(String.format("Can not find organizer with id '%s'", uid));
      return false;
    }
    organizer.verifyAccount();
    organizerRepository.save(organizer);
    return true;
  }

  private Organizer updateOrganizerInfo(Organizer organizer, Organizer newInfo) {
    organizer.setPhoneNumber(newInfo.getPhoneNumber());
    organizer.setEmail(newInfo.getEmail());
    organizer.setAddress(newInfo.getAddress());
    organizer.setBio(newInfo.getBio());
    organizer.setExtensions(newInfo.getExtensions());
    organizer.setDisplayName(newInfo.getDisplayName());

    return organizer;
  }
}
