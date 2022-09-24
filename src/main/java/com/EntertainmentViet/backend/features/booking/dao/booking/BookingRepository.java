package com.EntertainmentViet.backend.features.booking.dao.booking;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListOrganizerBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface BookingRepository extends IdentifiableRepository<Booking> {

    List<Booking> findByOrganizerUid(UUID organizerId, ListOrganizerBookingParamDto paramDto, Pageable pageable);

    List<Booking> findByTalentUid(UUID talentId, ListTalentBookingParamDto paramDto, Pageable pageable);
}
