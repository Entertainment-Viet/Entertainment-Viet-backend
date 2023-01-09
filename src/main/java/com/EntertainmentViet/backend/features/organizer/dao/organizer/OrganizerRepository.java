package com.EntertainmentViet.backend.features.organizer.dao.organizer;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ListOrganizerParamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface OrganizerRepository extends IdentifiableRepository<Organizer> {

    Page<Organizer> findAll(Pageable pageable, ListOrganizerParamDto paramDto);
}
