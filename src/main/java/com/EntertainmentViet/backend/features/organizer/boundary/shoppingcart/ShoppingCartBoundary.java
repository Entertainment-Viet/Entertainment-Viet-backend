package com.EntertainmentViet.backend.features.organizer.boundary.shoppingcart;

import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ChargeCartItemDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ListCartItemParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ReadCartItemDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.UpdateCartItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartBoundary {

    Page<ReadCartItemDto> findByOrganizerUid(UUID uid, ListCartItemParamDto paramDto, Pageable pageable);

    boolean charge(UUID organizerUid, ChargeCartItemDto chargeCartItemDto);

    boolean clear(UUID organizerUid);

    Optional<ReadCartItemDto> findByOrganizerUidAndUid(UUID organizerUid, UUID uid);

    Optional<UUID> update(UpdateCartItemDto updateCartItemDto, UUID organizerUid, UUID uid);

    boolean delete(UUID uid, UUID organizerUid);
}
