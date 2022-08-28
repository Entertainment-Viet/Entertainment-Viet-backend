package com.EntertainmentViet.backend.features.organizer.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
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

    private final JobDetailMapper jobDetailMapper;

    private final CategoryRepository categoryRepository;

    private final OrganizerRepository organizerRepository;


    @Override
    public List<JobOfferDto> findByOrganizerUid(UUID uid) {
        return jobOfferRepository.findByOrganizerUid(uid).stream().map(jobOfferMapper::toDto).toList();
    }

    @Override
    public Optional<JobOfferDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (organizer != null && jobOffer.getOrganizer().getUid().equals(organizerUid)) {
            return Optional.ofNullable(jobOfferMapper.toDto(jobOffer));
        }
        return Optional.empty();
    }

    @Override
    public Optional<UUID> create(JobOfferDto jobOfferDto, UUID organizerUid) {
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(jobOfferDto.getJobDetail());
        Category category = categoryRepository.findByUid(jobDetail.getCategory().getUid()).orElse(null);

        if (jobDetail == null || category == null || organizer == null) {
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
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (organizer != null && jobOffer != null && jobOffer.getOrganizer().getId().equals(organizer.getId())) {
            if (jobOffer != null) {
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

                jobOffer.setJobDetail(jobDetail);
                jobOffer.setName(jobOfferDto.getName());
                jobOffer.setIsActive(jobOfferDto.getIsActive());
                jobOffer.setQuantity(jobOfferDto.getQuantity());
                var newJobOffer = jobOfferRepository.save(jobOffer);
                return Optional.ofNullable(newJobOffer.getUid());
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(UUID uid, UUID organizerUid) {
        Organizer organizer = organizerRepository.findByUid(organizerUid).orElse(null);
        JobOffer jobOffer = jobOfferRepository.findByUid(uid).orElse(null);
        if (organizer != null && jobOffer != null && jobOffer.getOrganizer().getId().equals(organizer.getId())) {
            jobOfferRepository.deleteById(jobOfferRepository.findByUid(uid).orElse(null).getId());
        }
    }
}
