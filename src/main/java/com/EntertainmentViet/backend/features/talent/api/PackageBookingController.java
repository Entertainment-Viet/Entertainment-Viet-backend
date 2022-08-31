package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.talent.boundary.PackageBookingBoundary;
import com.EntertainmentViet.backend.features.talent.dto.CreatePackageBookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = PackageBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class PackageBookingController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages/{package_uid}/bookings";
  private final PackageBookingBoundary packageBookingService;

  @GetMapping()
  public CompletableFuture<List<BookingDto>> listBooking(@PathVariable("talent_uid") UUID talentId,
                                                         @PathVariable("package_uid") UUID packageId) {
    return CompletableFuture.completedFuture(packageBookingService.listBooking(talentId, packageId));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<Void>> addPackageToShoppingCart(@RequestBody @Valid CreatePackageBookingDto createPackageBookingDto,
          @PathVariable("talent_uid") UUID talentId,
          @PathVariable("package_uid") UUID packageId) {
    if (packageBookingService.addPackageToShoppingCart(talentId, packageId, createPackageBookingDto)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PostMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(
          @PathVariable("talent_uid") UUID talentId,
          @PathVariable("package_uid") UUID packageId,
          @PathVariable("uid") UUID bookingId) {
    if (packageBookingService.acceptBooking(talentId, packageId, bookingId)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(
          @PathVariable("talent_uid") UUID talentId,
          @PathVariable("package_uid") UUID packageId,
          @PathVariable("uid") UUID bookingId) {
    if (packageBookingService.rejectBooking(talentId, packageId, bookingId)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

}
