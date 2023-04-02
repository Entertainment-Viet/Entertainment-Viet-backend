package com.EntertainmentViet.backend.features.organizer.boundary.shoppingcart;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.organizer.OrganizerShoppingCart;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.booking.dao.booking.BookingRepository;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.notification.boundary.BookingNotifyBoundary;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dao.shoppingcart.ShoppingCartRepository;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.*;
import com.EntertainmentViet.backend.features.talent.dao.packagetalent.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService implements ShoppingCartBoundary {

    private final OrganizerRepository organizerRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    private final PackageRepository packageRepository;

    private final BookingRepository bookingRepository;

    private final CartItemMapper cartItemMapper;

    private final BookingNotifyBoundary bookingNotifyService;

    @Override
    public Page<ReadCartItemDto> findByOrganizerUid(UUID organizerUid, ListCartItemParamDto paramDto, Pageable pageable) {
        var dtoList = shoppingCartRepository.findByOrganizerUid(organizerUid, paramDto, pageable).stream()
            .map(cartItemMapper::toDto)
            .collect(Collectors.toList());

        return RestUtils.getPageEntity(dtoList, pageable);
    }

    @Override
    @Transactional
    public boolean charge(UUID organizerUid, ChargeCartItemDto chargeCartItemDto) {
        var organizerOptional = organizerRepository.findByUid(organizerUid);

        if (organizerOptional.isEmpty()) {
            log.warn(String.format("There was an invalid cart in shopping cart of organizer with uid '%s'", organizerUid));
            return false;
        }

        Organizer organizer = organizerOptional.get();
        if (!organizer.checkValidShoppingCart()) {
            return false;
        }

        for (OrganizerShoppingCart cartItem : organizer.getShoppingCart()) {
            var talentPackage = packageRepository.findByUid(cartItem.getTalentPackage().getUid()).orElse(null);

            List<Booking> createdOrders = cartItem.generateBooking(PaymentType.ofI18nKey(chargeCartItemDto.getPaymentType()));
            createdOrders.forEach(booking -> {
                    organizer.addBooking(booking);
                    talentPackage.addOrder(booking);
                    bookingRepository.save(booking);
            });
            packageRepository.save(talentPackage);

            if (createdOrders.isEmpty()) {
                log.info("There is no occurrence matching repeatPattern when charging cart item: " + cartItem.getUid());
            } else if (createdOrders.size() == 1) {
                var newBooking = createdOrders.get(0);
                bookingNotifyService.sendCreateNotification(newBooking.getTalent().getUid(), newBooking.getTalent().getDisplayName(), newBooking);
            } else {
                var firstBooking = createdOrders.get(0);
                bookingNotifyService.sendCreateRepeatNotification(firstBooking.getTalent().getUid(), firstBooking.getTalent().getDisplayName(), firstBooking, talentPackage.getRepeatPattern());
            }
        }
        organizer.clearCart();
        organizerRepository.save(organizer);
        return true;
    }

    @Override
    public boolean clear(UUID organizerUid) {
        var organizerOptional = organizerRepository.findByUid(organizerUid);

        if (organizerOptional.isEmpty()) {
            log.warn(String.format("There was an invalid cart in shopping cart of organizer with uid '%s'", organizerUid));
            return false;
        }

        try {
            Organizer organizer = organizerOptional.get();
            organizer.clearCart();
            organizerRepository.save(organizer);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Optional<ReadCartItemDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid) {
        OrganizerShoppingCart cartItem = shoppingCartRepository.findByUid(uid).orElse(null);
        if (!EntityValidationUtils.isCartItemWithUidExist(cartItem, uid)) {
            return Optional.empty();
        }
        if (!EntityValidationUtils.isCartItemBelongToOrganizerWithUid(cartItem, organizerUid)) {
            return Optional.empty();
        }

        return Optional.ofNullable(cartItemMapper.toDto(cartItem));
    }

    @Override
    public Optional<UUID> update(UpdateCartItemDto updateCartItemDto, UUID organizerUid, UUID uid) {
        OrganizerShoppingCart cartItem = shoppingCartRepository.findByUid(uid).orElse(null);
        if (!EntityValidationUtils.isCartItemWithUidExist(cartItem, uid)) {
            return Optional.empty();
        }
        if (!EntityValidationUtils.isCartItemBelongToOrganizerWithUid(cartItem, organizerUid)) {
            return Optional.empty();
        }

        OrganizerShoppingCart newCartItem = cartItemMapper.fromUpdateDtoToModel(updateCartItemDto);
        cartItem.updateInfo(newCartItem);
        return Optional.ofNullable(shoppingCartRepository.save(cartItem).getUid());
    }

    @Override
    public boolean delete(UUID uid, UUID organizerUid) {
        OrganizerShoppingCart cartItem = shoppingCartRepository.findByUid(uid).orElse(null);
        if (!EntityValidationUtils.isCartItemWithUidExist(cartItem, uid)) {
            return false;
        }
        if (!EntityValidationUtils.isCartItemBelongToOrganizerWithUid(cartItem, organizerUid)) {
            return false;
        }

        shoppingCartRepository.delete(cartItem);
        return true;
    }
}
