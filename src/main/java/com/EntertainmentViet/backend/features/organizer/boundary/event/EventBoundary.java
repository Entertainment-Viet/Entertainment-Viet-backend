package com.EntertainmentViet.backend.features.organizer.boundary.event;

import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.event.ReadEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventBoundary {

  Page<ReadEventDto> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable);

}
