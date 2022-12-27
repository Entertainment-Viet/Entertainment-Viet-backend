package com.EntertainmentViet.backend.features.organizer.boundary.joboffer;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.joboffer.JobOfferRepository;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobOfferService implements JobOfferBoundary {

    private final JobOfferRepository jobOfferRepository;

    private final OrganizerRepository organizerRepository;

    private final JobOfferMapper jobOfferMapper;

    @Override
    public Page<ReadJobOfferDto> findByOrganizerUid(UUID uid, ListJobOfferParamDto paramDto, Pageable pageable) {
        var dtoList = jobOfferRepository.findByOrganizerUid(uid, paramDto, pageable).stream()
                .map(jobOfferMapper::toDto)
                .collect(Collectors.toList());
        return RestUtils.getPageEntity(dtoList, pageable);
    }

    @Override
    public Optional<ReadJobOfferDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (!EntityValidationUtils.isJobOfferWithUidExist(jobOffer, uid)) {
            return Optional.empty();
        }

        if (!EntityValidationUtils.isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jobOfferMapper.toDto(jobOffer));
    }

    @Override
    @Transactional
    public Optional<UUID> create(CreateJobOfferDto createJobOfferDto, UUID organizerUid) {
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobOffer jobOffer = jobOfferMapper.fromCreateDtoToModel(createJobOfferDto);
        jobOffer.setOrganizer(organizer);

        if (!EntityValidationUtils.isJobOfferValid(jobOffer)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jobOfferRepository.save(jobOffer).getUid());
    }

    @Override
    public Optional<UUID> update(UpdateJobOfferDto updateJobOfferDto, UUID organizerUid, UUID uid) {
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);

        if (!EntityValidationUtils.isJobOfferWithUidExist(jobOffer, uid)) {
            return Optional.empty();
        }

        if (!EntityValidationUtils.isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return Optional.empty();
        }

        JobOffer newJobOffer = jobOfferMapper.fromUpdateDtoToModel(updateJobOfferDto);
        jobOffer.updateInfo(newJobOffer);
        return Optional.ofNullable(jobOfferRepository.save(jobOffer).getUid());
    }

    @Override
    public boolean delete(UUID uid, UUID organizerUid) {
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (!EntityValidationUtils.isJobOfferWithUidExist(jobOffer, uid)) {
            return false;
        }

        if (!EntityValidationUtils.isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return false;
        }

        jobOffer.setArchived(Boolean.TRUE);
        jobOfferRepository.save(jobOffer);
        return true;
    }
}
