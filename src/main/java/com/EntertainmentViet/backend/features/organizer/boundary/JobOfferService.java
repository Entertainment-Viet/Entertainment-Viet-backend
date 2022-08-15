package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.booking.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.booking.dto.JobOfferMapper;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobOfferService implements JobOfferBoundary {

    private final JobOfferRepository jobOfferRepository;

    private final JobOfferMapper jobOfferMapper;

    @Override
    public List<JobOfferDto> findByOrganizerUid(UUID uid) {
        return jobOfferRepository.findByOrganizerUid(uid).stream().map(jobOfferMapper::toDto).toList();
    }

    @Override
    public JobOfferDto findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
        return jobOfferMapper.toDto(jobOfferRepository.findByOrganizerUidAndUid(organizerUid, uid).orElse(null));
    }
}
