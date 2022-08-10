package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizerService implements OrganizerBoundary {

  private final OrganizerRepository organizerRepository;

  private final OrganizerMapper organizerMapper;

  @Override
  public OrganizerDto findByUid(UUID id) {
    return organizerRepository.findByUid(id).map(organizerMapper::toDto).orElse(null);
  }

  @Override
  public OrganizerDto create(OrganizerDto organizerDto) {
    var newOrganizer = organizerRepository.save(organizerMapper.toModel(organizerDto));
    return organizerMapper.toDto(newOrganizer);
  }
}
