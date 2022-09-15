package com.EntertainmentViet.backend.features.booking.dao.jobdetail;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.features.common.dao.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface JobDetailRepository extends BaseRepository<JobDetail, Long> {

    Optional<JobDetail> findById(Long id);
}
