package com.EntertainmentViet.backend.features.talent.dao.talent;

import javax.transaction.Transactional;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TalentRepository extends IdentifiableRepository<Talent> {

    Page<Talent> findAll(ListTalentParamDto paramDto, Pageable pageable);
}
