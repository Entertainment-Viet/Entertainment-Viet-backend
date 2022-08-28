package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.organizer.dao.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.PackageRepository;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.PackageBookingDto;
import com.EntertainmentViet.backend.features.talent.dto.PackageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PackageBookingService implements PackageBookingBoundary {

    private final PackageRepository packageRepository;

    private final PackageMapper packageMapper;

    private final TalentRepository talentRepository;

    private final OrganizerRepository organizerRepository;


    @Override
    public Boolean addPackageToShoppingCart(UUID talentId, UUID packageId, PackageBookingDto packageBookingDto) {
        Package packageTalent = packageRepository.findByUid(packageBookingDto.getPackageId()).orElse(null);
        Organizer organizer = organizerRepository.findByUid(packageBookingDto.getOrganizerId()).orElse(null);
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        if (packageTalent != null && organizer != null && talent != null && packageTalent.getTalent().getId().equals(talent.getId())) {
            organizer.addPackageToCart(packageTalent);
            organizerRepository.save(organizer);
            return true;
        }
        return false;
    }

    @Override
    public List<BookingDto> listBooking(UUID talentId, UUID packageId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (talent != null && packageTalent != null && packageTalent.getTalent().getId().equals(talent.getId())) {
            return packageRepository.findByUid(packageId).map(packageMapper::toDto).orElse(null).getOrders();
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<UUID> acceptBooking(UUID talentId, UUID packageId, UUID bookingId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (talent != null && packageTalent != null && packageTalent.getTalent().getId().equals(talent.getId())) {
            talent.acceptBooking(bookingId);
            talentRepository.save(talent);
            return Optional.ofNullable(bookingId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UUID> rejectBooking(UUID talentId, UUID packageId, UUID bookingId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (talent != null && packageTalent != null && packageTalent.getTalent().getId().equals(talent.getId())) {
            talent.rejectBooking(bookingId);
            talentRepository.save(talent);
            return Optional.ofNullable(bookingId);
        }
        return Optional.empty();
    }
}
