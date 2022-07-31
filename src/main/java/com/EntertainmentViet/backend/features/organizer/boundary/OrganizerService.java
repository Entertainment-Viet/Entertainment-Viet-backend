package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.OrganizerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizerService implements OrganizerBoundary {

  private final OrganizerRepository organizerRepository;

  private final OrganizerMapper organizerMapper;

  @Override
  public OrganizerDto findByUid(Long id) {
    return organizerRepository.findById(id).map(organizerMapper::toDto).orElse(null);
  }

  @Override
  public Long create(OrganizerDto organizerDto) {
    var newOrganizer = organizerRepository.save(organizerMapper.toModel(organizerDto));
    return newOrganizer.getId();
  }
}
