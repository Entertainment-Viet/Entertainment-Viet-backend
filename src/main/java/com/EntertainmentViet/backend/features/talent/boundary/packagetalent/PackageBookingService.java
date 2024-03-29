package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.notification.boundary.BookingNotifyBoundary;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.AddCartItemDto;
import com.EntertainmentViet.backend.features.talent.dao.packagetalent.PackageRepository;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final BookingNotifyBoundary bookingNotifyService;

    @Override
    public boolean addPackageToShoppingCart(UUID talentId, UUID packageId, AddCartItemDto addCartItemDto) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        Organizer organizer = organizerRepository.findByUid(addCartItemDto.getOrganizerId()).orElse(null);
        if (!EntityValidationUtils.isOrganizerWithUid(organizer, addCartItemDto.getOrganizerId())) {
            return false;
        }

        if (!EntityValidationUtils.isPackageWithUidExist(packageTalent, packageId)) {
            return false;
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return false;
        }
        // check if the package is already in shopping cart
        if (!organizer.addPackageToCart(packageTalent, addCartItemDto.getSuggestedPrice())) {
            return false;
        }
        organizerRepository.save(organizer);
        return true;
    }

    @Override
    public List<ReadBookingDto> listBooking(UUID talentId, UUID packageId) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (!EntityValidationUtils.isPackageWithUidExist(packageTalent, packageId)) {
            return Collections.emptyList();
        }

        if (EntityValidationUtils.isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return packageTalent.getOrders().stream().map(bookingMapper::toReadDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public Optional<List<UUID>> create(UUID talentId, UUID packageId, CreatePackageOrderDto createPackageOrderDto) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        Organizer organizer = organizerRepository.findByUid(createPackageOrderDto.getOrganizerId()).orElse(null);
        if (!EntityValidationUtils.isOrganizerWithUid(organizer, createPackageOrderDto.getOrganizerId())) {
            return Optional.empty();
        }

        if (!EntityValidationUtils.isPackageWithUidExist(packageTalent, packageId)) {
            return Optional.empty();
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return Optional.empty();
        }

        JobDetail jobDetail = jobDetailMapper.fromCreateDtoToModel(createPackageOrderDto.getJobDetail());
        List<Booking> createdOrders = packageTalent.generateOrder(organizer, jobDetail, PaymentType.ofI18nKey(createPackageOrderDto.getPaymentType()));

        List<UUID> createdUid = createdOrders.stream()
            .map(bookingRepository::save)
            .map(Identifiable::getUid)
            .collect(Collectors.toList());
        packageRepository.save(packageTalent);

        if (createdOrders.isEmpty()) {
            throw new InvalidInputException("There is no occurrence matching repeatPattern");
        } else if (createdOrders.size() == 1) {
            var newBooking = createdOrders.get(0);
            bookingNotifyService.sendCreateNotification(newBooking.getTalent().getUid(), newBooking.getTalent().getDisplayName(), newBooking);
        } else {
            var firstBooking = createdOrders.get(0);
            bookingNotifyService.sendCreateRepeatNotification(firstBooking.getTalent().getUid(), firstBooking.getTalent().getDisplayName(), firstBooking, packageTalent.getRepeatPattern());
        }
        return Optional.of(createdUid);
    }

    @Override
    public boolean acceptBooking(UUID talentId, UUID packageId, UUID bookingId) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (!EntityValidationUtils.isPackageWithUidExist(packageTalent, packageId)) {
            return false;
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return false;
        }

        var acceptedBooking = packageTalent.getOrders().stream().filter(booking -> booking.getUid().equals(bookingId)).findAny().orElse(null);
        bookingNotifyService.sendAcceptNotification(acceptedBooking.getTalent().getUid(), acceptedBooking.getTalent().getDisplayName(), acceptedBooking);
        bookingNotifyService.sendAcceptNotification(acceptedBooking.getOrganizer().getUid(), acceptedBooking.getOrganizer().getDisplayName(), acceptedBooking);

        packageTalent.acceptOrder(bookingId);
        packageRepository.save(packageTalent);
        return true;
    }

    @Override
    public boolean rejectBooking(UUID talentId, UUID packageId, UUID bookingId) {
        Package packageTalent = packageRepository.findByUid(packageId).orElse(null);
        if (!EntityValidationUtils.isPackageWithUidExist(packageTalent, packageId)) {
            return false;
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return false;
        }

        var acceptedBooking = packageTalent.getOrders().stream().filter(booking -> booking.getUid().equals(bookingId)).findAny().orElse(null);
        bookingNotifyService.sendRejectNotification(acceptedBooking.getTalent().getUid(), acceptedBooking.getTalent().getDisplayName(), acceptedBooking);
        bookingNotifyService.sendRejectNotification(acceptedBooking.getOrganizer().getUid(), acceptedBooking.getOrganizer().getDisplayName(), acceptedBooking);

        packageTalent.rejectOrder(bookingId);
        packageRepository.save(packageTalent);
        return true;
    }
}
