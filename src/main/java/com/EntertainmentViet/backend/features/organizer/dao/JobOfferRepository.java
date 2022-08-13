package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface JobOfferRepository extends IdentifiableRepository<JobOffer> {

    @Query("select j " +
            "from JobOffer j " +
            "where j.organizer.uid = :uid ")
    List<JobOffer> findByOrganizerUid(UUID uid);


    @Query("select j " +
            "from JobOffer j " +
            "where j.organizer.uid = :organizerUid and j.uid = :uid ")
    JobOffer findByOrganizerUidAndUid(UUID organizerUid, UUID uid);
}
