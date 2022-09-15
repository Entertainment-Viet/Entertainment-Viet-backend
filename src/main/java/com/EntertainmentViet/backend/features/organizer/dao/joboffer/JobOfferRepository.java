package com.EntertainmentViet.backend.features.organizer.dao.joboffer;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface JobOfferRepository extends IdentifiableRepository<JobOffer> {

    List<JobOffer> findByOrganizerUid(UUID uid);

    Optional<JobOffer> findByOrganizerUidAndUid(UUID organizerUid, UUID uid);
}
