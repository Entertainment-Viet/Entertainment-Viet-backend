package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.booking.dao.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public BookingDto findByUid(UUID uid) {
        return bookingMapper.toDto(bookingRepository.findByUid(uid).orElse(null));
    }

    @Override
    public BookingDto create(BookingDto bookingDto) {
        Organizer organizer = organizerRepository.findByUid(bookingDto.getOrganizerUid()).get();
        Talent talent = talentRepository.findByUid(bookingDto.getTalentUid()).get();

        JobDetailDto jobDetailDto = bookingDto.getJobDetailDto();

        JobDetail jobDetail = jobDetailRepository.save(jobDetailMapper.toModel(jobDetailDto));

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
