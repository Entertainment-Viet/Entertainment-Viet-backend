package com.EntertainmentViet.backend.features.organizer.dao.organizer;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface OrganizerRepository extends IdentifiableRepository<Organizer> {
}
