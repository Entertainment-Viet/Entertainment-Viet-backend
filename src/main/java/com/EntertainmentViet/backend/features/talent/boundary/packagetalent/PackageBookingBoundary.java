package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.AddCartItemDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageOrderDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageBookingBoundary {

    boolean addPackageToShoppingCart(UUID talentId, UUID packageId, AddCartItemDto addCartItemDto);

    List<ReadBookingDto> listBooking(UUID talentId, UUID packageId);

    Optional<List<UUID>> create(UUID talentId, UUID packageId, CreatePackageOrderDto createPackageOrderDto);

    boolean acceptBooking(UUID talentId, UUID packageId, UUID bookingId);

    boolean rejectBooking(UUID talentId, UUID packageId, UUID bookingId);
}
