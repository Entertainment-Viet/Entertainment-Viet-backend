package com.EntertainmentViet.backend.features.organizer.boundary.shoppingcart;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.CartItemMapper;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ChargeCartItemDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ReadCartItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService implements ShoppingCartBoundary {

    private final OrganizerRepository organizerRepository;

    private final CartItemMapper cartItemMapper;

    @Override
    public List<ReadCartItemDto> findByOrganizerUid(UUID organizerUid) {
        return organizerRepository.findByUid(organizerUid)
            .map(Organizer::getShoppingCart)
            .orElse(Collections.emptyList())
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean charge(UUID organizerUid, ChargeCartItemDto chargeCartItemDto) {
        var organizerOptional = organizerRepository.findByUid(organizerUid);

        if (organizerOptional.isEmpty()) {
            log.warn(String.format("There was an invalid cart in shopping cart of organizer with uid '%s'", organizerUid));
            return false;
        }

        try {
            Organizer organizer = organizerOptional.get();
            organizer.finishCartShopping(PaymentType.ofI18nKey(chargeCartItemDto.getPaymentType()));
            organizerRepository.save(organizer);
        } catch (EntityNotFoundException ex) {
            log.warn(ex.getMessage());
            return false;
        }
        return true;
    }
}
