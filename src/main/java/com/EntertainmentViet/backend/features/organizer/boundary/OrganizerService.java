package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerService implements OrganizerBoundary {

  private final OrganizerRepository organizerRepository;

  private final OrganizerMapper organizerMapper;

  @Override
  public Optional<OrganizerDto> findByUid(UUID id) {
    return organizerRepository.findByUid(id).map(organizerMapper::toDto);
  }

  @Override
  public Optional<UUID> create(OrganizerDto organizerDto) {
    organizerDto.setUid(null);  // Remove UID pass by user when create
    var newOrganizer = organizerRepository.save(organizerMapper.toModel(organizerDto));
    return Optional.ofNullable(newOrganizer.getUid());
  }

  @Override
  public Optional<UUID> update(OrganizerDto organizerDto, UUID uid) {
    return organizerRepository.findByUid(uid)
        .map(organizer -> updateOrganizerInfo(organizer, organizerMapper.toModel(organizerDto)))
        .map(organizerRepository::save)
        .map(Identifiable::getUid);
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
