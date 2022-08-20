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
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
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

    private final JobDetailRepository jobDetailRepository;

    private final JobDetailMapper jobDetailMapper;

    private final CategoryRepository categoryRepository;

    @Override
    public BookingDto findByUid(UUID uid) {
        return bookingMapper.toDto(bookingRepository.findByUid(uid).orElse(null));
    }

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto) {
        Organizer organizer = organizerRepository.findByUid(bookingDto.getOrganizerUid()).orElse(null);
        Talent talent = talentRepository.findByUid(bookingDto.getTalentUid()).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(bookingDto.getJobDetailDto());
        Category category = categoryRepository.findByName(jobDetail.getCategory().getName()).orElse(null);
        jobDetail.setCategory(category);

        Booking booking = bookingMapper.toModel(bookingDto);
        booking.setOrganizer(organizer);
        booking.setTalent(talent);
        booking.setJobDetail(jobDetail);

        var newBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(newBooking);
    }

    @Override
    public BookingDto update(BookingDto bookingDto, UUID uid) {
        return null;
    }
}
