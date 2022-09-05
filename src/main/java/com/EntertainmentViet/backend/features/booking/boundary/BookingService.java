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

import java.util.Optional;
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
    public Optional<BookingDto> findByUid(UUID ownerUid, UUID uid) {
        return bookingRepository.findByUid(uid)
            .filter(booking -> booking.getTalent().getUid().equals(ownerUid) || booking.getOrganizer().getUid().equals(ownerUid))
            .map(bookingMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<UUID> create(UUID ownerUid, BookingDto bookingDto) {
        Organizer organizer = organizerRepository.findByUid(bookingDto.getOrganizerUid()).orElse(null);
        Talent talent = talentRepository.findByUid(bookingDto.getTalentUid()).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(bookingDto.getJobDetailDto());
        Category category = categoryRepository.findByUid(jobDetail.getCategory().getUid()).orElse(null);

        if (organizer == null || talent == null || jobDetail == null || category == null) {
            Optional.empty();
        }

        if (!ownerUid.equals(talent.getUid()) && !ownerUid.equals(organizer.getUid())) {
            return Optional.empty();
        }

        jobDetail.setCategory(category);
        Booking booking = bookingMapper.toModel(bookingDto);
        booking.setOrganizer(organizer);
        booking.setTalent(talent);
        booking.setJobDetail(jobDetail);
        booking.setId(null);
        booking.setUid(null);

        var newBooking = bookingRepository.save(booking);
        return Optional.ofNullable(newBooking.getUid());
    }

    @Override
    public Optional<UUID> update(UUID ownerUid, UUID uid, BookingDto bookingDto) {
        var bookingOptional = bookingRepository.findByUid(uid);
        if (!bookingOptional.isPresent()) {
            return Optional.empty();
        }

        Booking updatingBooking = bookingOptional.get();
        if (!ownerUid.equals(updatingBooking.getTalent().getUid()) && !ownerUid.equals(updatingBooking.getOrganizer().getUid())) {
            return Optional.empty();
        }

        JobDetail jobDetail = updatingBooking.getJobDetail();
        Category category = categoryRepository.findByUid(bookingDto.getJobDetailDto().getCategory().getUid()).orElse(null);
        jobDetail.setCategory(category);

        JobDetail jobDetailUpdate = jobDetailMapper.toModel(bookingDto.getJobDetailDto());
        jobDetail.setPrice(jobDetailUpdate.getPrice());
        jobDetail.setWorkType(jobDetailUpdate.getWorkType());
        jobDetail.setPerformanceTime(jobDetailUpdate.getPerformanceTime());
        jobDetail.setPerformanceDuration(jobDetailUpdate.getPerformanceDuration());
        jobDetail.setNote(jobDetailUpdate.getNote());
        jobDetail.setExtensions(jobDetailUpdate.getExtensions());
        jobDetail.setLocation(jobDetailUpdate.getLocation());

        updatingBooking.setJobDetail(jobDetail);
        var newBooking = bookingRepository.save(updatingBooking);
        return Optional.ofNullable(newBooking.getUid());
    }
}
