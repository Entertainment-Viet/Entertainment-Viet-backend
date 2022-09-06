package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.JobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.JobOfferMapper;
import com.EntertainmentViet.backend.features.organizer.dao.JobOfferRepository;
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

    private final JobOfferMapper jobOfferMapper;

    private final JobDetailMapper jobDetailMapper;

    private final CategoryRepository categoryRepository;

    private final OrganizerRepository organizerRepository;


    @Override
    public List<JobOfferDto> findByOrganizerUid(UUID uid) {
        return jobOfferRepository.findByOrganizerUid(uid).stream().map(jobOfferMapper::toDto).toList();
    }

    @Override
    public Optional<JobOfferDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
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
    public Optional<UUID> create(JobOfferDto jobOfferDto, UUID organizerUid) {
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(jobOfferDto.getJobDetail());
        Category category = categoryRepository.findByUid(jobDetail.getCategory().getUid()).orElse(null);

        if (jobDetail == null || category == null || organizer == null) {
            log.warn("Can not find required information to create new job-offer");
            Optional.empty();
        }

        jobDetail.setCategory(category);
        JobOffer jobOffer = jobOfferMapper.toModel(jobOfferDto);
        jobOffer.setOrganizer(organizer);
        jobOffer.setJobDetail(jobDetail);
        jobOffer.setId(null);
        jobOffer.setUid(null);

        var newJobOffer = jobOfferRepository.save(jobOffer);
        return Optional.ofNullable(newJobOffer.getUid());
    }

    @Override
    public Optional<UUID> update(JobOfferDto jobOfferDto, UUID organizerUid, UUID uid) {
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (!isJobOfferWithUidExist(jobOffer, uid)) {
            return Optional.empty();
        }

        if (!isJobOfferBelongToOrganizerWithUid(jobOffer, organizerUid)) {
            return Optional.empty();
        }

        JobDetail jobDetail = jobOffer.getJobDetail();
        Category category = categoryRepository.findByUid(jobOfferDto.getJobDetail().getCategory().getUid()).orElse(null);
        jobDetail.setCategory(category);

        JobDetail jobDetailUpdate = jobDetailMapper.toModel(jobOfferDto.getJobDetail());
        jobDetail.setPrice(jobDetailUpdate.getPrice());
        jobDetail.setWorkType(jobDetailUpdate.getWorkType());
        jobDetail.setPerformanceTime(jobDetailUpdate.getPerformanceTime());
        jobDetail.setPerformanceDuration(jobDetailUpdate.getPerformanceDuration());
        jobDetail.setNote(jobDetailUpdate.getNote());
        jobDetail.setExtensions(jobDetailUpdate.getExtensions());
        jobDetail.setLocation(jobDetailUpdate.getLocation());

        jobOffer.setJobDetail(jobDetail);
        jobOffer.setName(jobOfferDto.getName());
        jobOffer.setIsActive(jobOfferDto.getIsActive());
        jobOffer.setQuantity(jobOfferDto.getQuantity());
        var newJobOffer = jobOfferRepository.save(jobOffer);
        return Optional.ofNullable(newJobOffer.getUid());
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

        try {
            jobOfferRepository.deleteById(jobOffer.getId());
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean isJobOfferWithUidExist(JobOffer jobOffer, UUID uid) {
        if (jobOffer == null) {
            log.warn(String.format("Can not find job-offer with id '%s'", uid));
            return false;
        }
        return true;
    }
    private boolean isJobOfferBelongToOrganizerWithUid(JobOffer jobOffer, UUID organizerUid) {
        if (jobOffer.getOrganizer() == null) {
            log.warn(String.format("Can not find organizer owning the joboffer with id '%s'", jobOffer.getUid()));
            return false;
        }

        Organizer organizer = jobOffer.getOrganizer();
        if (!organizer.getUid().equals(organizerUid)) {
            log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", jobOffer.getUid(), organizerUid));
            return false;
        }
        return true;
    }
}
