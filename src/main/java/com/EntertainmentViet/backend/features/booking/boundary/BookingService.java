package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dao.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService implements BookingBoundary {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final OrganizerRepository organizerRepository;

    private final TalentRepository talentRepository;

    private final JobDetailMapper jobDetailMapper;

    private final CategoryRepository categoryRepository;

    @Override
    public BookingDto findByUid(UUID uid) {
        return bookingMapper.toDto(bookingRepository.findByUid(uid).orElse(null));
    }

    @Override
    @Transactional
    public UUID create(BookingDto bookingDto) throws Exception {
        Organizer organizer = organizerRepository.findByUid(bookingDto.getOrganizerUid()).orElse(null);
        Talent talent = talentRepository.findByUid(bookingDto.getTalentUid()).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(bookingDto.getJobDetailDto());
        Category category = categoryRepository.findByName(jobDetail.getCategory().getName()).orElse(null);

        if (organizer == null || talent == null || jobDetail == null || category == null) {
            throw new Exception();
        }

        jobDetail.setCategory(category);
        Booking booking = bookingMapper.toModel(bookingDto);
        booking.setOrganizer(organizer);
        booking.setTalent(talent);
        booking.setJobDetail(jobDetail);

        var newBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(newBooking).getUid();
    }

    @Override
    public UUID update(BookingDto bookingDto, UUID uid) throws Exception {
        Booking bookingCheck = bookingRepository.findByUid(uid).orElse(null);
        if (bookingCheck != null) {
            JobDetail jobDetail = bookingCheck.getJobDetail();
            Category category = categoryRepository.findByName(bookingDto.getJobDetailDto().getCategory().getName()).orElse(null);
            jobDetail.setCategory(category);

            JobDetail jobDetailUpdate = jobDetailMapper.toModel(bookingDto.getJobDetailDto());
            jobDetail.setPrice(jobDetailUpdate.getPrice());
            jobDetail.setWorkType(jobDetailUpdate.getWorkType());
            jobDetail.setPerformanceTime(jobDetailUpdate.getPerformanceTime());
            jobDetail.setPerformanceDuration(jobDetailUpdate.getPerformanceDuration());
            jobDetail.setNote(jobDetailUpdate.getNote());
            jobDetail.setExtensions(jobDetailUpdate.getExtensions());

            bookingCheck.setJobDetail(jobDetail);
            var newBooking = bookingRepository.save(bookingCheck);
            return newBooking.getUid();
        }
        throw new Exception();
    }
}
