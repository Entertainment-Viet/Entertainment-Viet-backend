package com.EntertainmentViet.backend.features.organizer.boundary.joboffer;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.category.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import com.EntertainmentViet.backend.features.organizer.dao.joboffer.JobOfferRepository;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.UpdateJobOfferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobOfferService implements JobOfferBoundary {

    private final JobOfferRepository jobOfferRepository;

    private final OrganizerRepository organizerRepository;

    private final JobOfferMapper jobOfferMapper;

    @Override
    public List<ReadJobOfferDto> findByOrganizerUid(UUID uid) {
        return jobOfferRepository.findByOrganizerUid(uid).stream().map(jobOfferMapper::toDto).toList();
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
