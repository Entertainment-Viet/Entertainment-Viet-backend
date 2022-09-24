package com.EntertainmentViet.backend.features.organizer.boundary.joboffer;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dao.joboffer.JobOfferRepository;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ListJobOfferParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.UpdateJobOfferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        if (!isJobOfferWithUidExist(jobOffer, uid)) {
            return Optional.empty();
        }

        if (!isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jobOfferMapper.toDto(jobOffer));
    }

    @Override
    public Optional<UUID> create(CreateJobOfferDto createJobOfferDto, UUID organizerUid) {
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobOffer jobOffer = jobOfferMapper.fromCreateDtoToModel(createJobOfferDto);
        jobOffer.setOrganizer(organizer);

        if (!isJobOfferValid(jobOffer)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jobOfferRepository.save(jobOffer).getUid());
    }

    @Override
    public Optional<UUID> update(UpdateJobOfferDto updateJobOfferDto, UUID organizerUid, UUID uid) {
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);

        if (!isJobOfferWithUidExist(jobOffer, uid)) {
            return Optional.empty();
        }

        if (!isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return Optional.empty();
        }

        JobOffer newJobOffer = jobOfferMapper.fromUpdateDtoToModel(updateJobOfferDto);
        jobOffer.updateInfo(newJobOffer);
        return Optional.ofNullable(jobOfferRepository.save(jobOffer).getUid());
    }

    @Override
    public boolean delete(UUID uid, UUID organizerUid) {
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (!isJobOfferWithUidExist(jobOffer, uid)) {
            return false;
        }

        if (!isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return false;
        }

        jobOfferRepository.deleteById(jobOffer.getId());
        return true;
    }

    private boolean isJobOfferWithUidExist(JobOffer jobOffer, UUID uid) {
        if (jobOffer == null) {
            log.warn(String.format("Can not find jobOffer with id '%s'", uid));
            return false;
        }
        return true;
    }

    private boolean isJobOfferBelongToOrganizerWithUid(JobOffer jobOffer, UUID organizerUid) {
        Organizer organizer = jobOffer.getOrganizer();
        if (!organizer.getUid().equals(organizerUid)) {
            log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", jobOffer.getUid(), organizerUid));
            return false;
        }
        return true;
    }

    private boolean isJobOfferValid(JobOffer jobOffer) {
        if (jobOffer.getOrganizer() == null) {
            log.warn(String.format("Can not find organizer owning the jobOffer with id '%s'", jobOffer.getUid()));
            return false;
        }

        if (jobOffer.getJobDetail() == null || jobOffer.getJobDetail().getCategory() == null) {
            log.warn(String.format("Can not populate jobDetail information for jobOffer with id '%s'", jobOffer.getUid()));
            return false;
        }
        return true;
    }
}
