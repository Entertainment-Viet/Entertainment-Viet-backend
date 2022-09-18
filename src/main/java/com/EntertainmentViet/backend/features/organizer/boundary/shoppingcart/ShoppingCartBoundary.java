package com.EntertainmentViet.backend.features.organizer.boundary.shoppingcart;

import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.UpdateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ReadCartItemDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartBoundary {

    List<ReadCartItemDto> findByOrganizerUid(UUID uid);

    boolean charge(UUID organizerUid);
}
