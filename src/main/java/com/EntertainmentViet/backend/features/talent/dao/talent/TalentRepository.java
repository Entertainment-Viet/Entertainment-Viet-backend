package com.EntertainmentViet.backend.features.talent.dao.talent;

import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface TalentRepository extends IdentifiableRepository<Talent> {

    Page<Talent> findAll(Pageable pageable);

    Optional<Talent> findByUid(UUID uid);
}
