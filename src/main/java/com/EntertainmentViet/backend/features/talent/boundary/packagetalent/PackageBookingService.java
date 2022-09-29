package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.packagetalent.PackageRepository;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageOrderDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.AddCartItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PackageBookingService implements PackageBookingBoundary {

    private final PackageRepository packageRepository;

    private final OrganizerRepository organizerRepository;

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final JobDetailMapper jobDetailMapper;

    @Override
    public boolean addPackageToShoppingCart(UUID talentId, UUID packageId, AddCartItemDto addCartItemDto) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        Organizer organizer = organizerRepository.findByUid(addCartItemDto.getOrganizerId()).orElse(null);
        if (organizer == null) {
            log.warn(String.format("Can not find organizer with id '%s'", addCartItemDto.getOrganizerId()));
            return false;
        }

        if (!isPackageWithUidExist(packageTalent, packageId)) {
            return false;
        }

        if (!isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return false;
        }
        organizer.addPackageToCart(packageTalent);
        organizerRepository.save(organizer);
        return true;
    }

    @Override
    public List<ReadBookingDto> listBooking(UUID talentId, UUID packageId) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (!isPackageWithUidExist(packageTalent, packageId)) {
            return Collections.emptyList();
        }

        if (isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return packageTalent.getOrders().stream().map(bookingMapper::toReadDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<UUID> create(UUID talentId, UUID packageId, CreatePackageOrderDto createPackageOrderDto) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        Organizer organizer = organizerRepository.findByUid(createPackageOrderDto.getOrganizerId()).orElse(null);
        if (organizer == null) {
            log.warn(String.format("Can not find organizer with id '%s'", createPackageOrderDto.getOrganizerId()));
            return Optional.empty();
        }

        if (!isPackageWithUidExist(packageTalent, packageId)) {
            return Optional.empty();
        }

        if (!isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return Optional.empty();
        }

        Booking createdOrder = packageTalent.orderPackage(organizer, PaymentType.ofI18nKey(createPackageOrderDto.getPaymentType()));
        var newBooking = bookingRepository.save(createdOrder);
        createdOrder.setJobDetail(jobDetailMapper.fromCreateDtoToModel(createPackageOrderDto.getJobDetail()));

        packageRepository.save(packageTalent);
        return Optional.of(newBooking.getUid());
    }

    @Override
    public boolean acceptBooking(UUID talentId, UUID packageId, UUID bookingId) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (!isPackageWithUidExist(packageTalent, packageId)) {
            return false;
        }

        if (!isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return false;
        }

        try {
            packageTalent.acceptOrder(bookingId);
            packageRepository.save(packageTalent);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean rejectBooking(UUID talentId, UUID packageId, UUID bookingId) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (!isPackageWithUidExist(packageTalent, packageId)) {
            return false;
        }

        if (!isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return false;
        }

        try {
            packageTalent.rejectOrder(bookingId);
            packageRepository.save(packageTalent);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean isPackageWithUidExist(Package packageTalent, UUID uid) {
        if (packageTalent == null) {
            log.warn(String.format("Can not find package with id '%s' ", uid));
            return false;
        }
        return true;
    }

    private boolean isPackageBelongToTalentWithUid(Package packageTalent, UUID talentId) {
        if (packageTalent.getTalent() == null) {
            log.warn(String.format("Can not find talent owning the package with id '%s'", packageTalent.getUid()));
            return false;
        }

        Talent talent = packageTalent.getTalent();
        if (!talent.getUid().equals(talentId)) {
            log.warn(String.format("There is no talent with id '%s' have the package with id '%s'", talentId, packageTalent.getUid()));
            return false;
        }
        return true;
    }
}
