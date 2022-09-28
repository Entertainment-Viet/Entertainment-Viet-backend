package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.event.EventRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.EventMapper;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService implements EventBoundary {

  private final EventRepository eventRepository;

  private final EventMapper eventMapper;

  @Override
  public Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable) {
    var dtoList = eventRepository.findByOrganizerUid(uid, paramDto, pageable).stream()
        .map(eventMapper::toDto)
        .collect(Collectors.toList());

    return RestUtils.getPageEntity(dtoList, pageable);
  }
}
