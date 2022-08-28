package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.talent.dto.PackageBookingDto;
import com.EntertainmentViet.backend.features.talent.dto.PackageDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageBookingBoundary {

    Boolean addPackageToShoppingCart(UUID talentId, UUID packageId, PackageBookingDto packageBookingDto);

    List<BookingDto> listBooking(UUID talentId, UUID packageId);

    Optional<UUID> acceptBooking(UUID talentId, UUID packageId, UUID bookingId);

    Optional<UUID> rejectBooking(UUID talentId, UUID packageId, UUID bookingId);
}
