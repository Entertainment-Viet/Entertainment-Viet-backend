package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventParamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends IdentifiableRepository<Event> {

  Page<Event> findAll(ListEventParamDto paramDto, Pageable pageable);

  List<Event> findByOrganizerUid(UUID uid, ListEventParamDto paramDto, Pageable pageable);
}
