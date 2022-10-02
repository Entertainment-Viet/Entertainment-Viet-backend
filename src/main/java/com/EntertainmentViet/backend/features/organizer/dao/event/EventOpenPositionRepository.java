package com.EntertainmentViet.backend.features.organizer.dao.event;

import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import com.EntertainmentViet.backend.features.organizer.dto.event.ListEventPositionParamDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EventOpenPositionRepository extends IdentifiableRepository<EventOpenPosition> {

  List<EventOpenPosition> findByOrganizerUidAndEventUid(UUID organizerUid, UUID eventUid, ListEventPositionParamDto paramDto, Pageable pageable);
}
