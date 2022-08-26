package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.features.organizer.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.JobOfferMapper;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public Optional<JobOfferDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
        return jobOfferRepository.findByUid(uid).map(jobOfferMapper::toDto);
    }
}
