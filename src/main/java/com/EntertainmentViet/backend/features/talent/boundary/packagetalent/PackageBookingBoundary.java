package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageBookingDto;

import java.util.List;
import java.util.UUID;

public interface PackageBookingBoundary {

    boolean addPackageToShoppingCart(UUID talentId, UUID packageId, CreatePackageBookingDto createPackageBookingDto);

    List<ReadBookingDto> listBooking(UUID talentId, UUID packageId);

    boolean acceptBooking(UUID talentId, UUID packageId, UUID bookingId);

    boolean rejectBooking(UUID talentId, UUID packageId, UUID bookingId);
}
