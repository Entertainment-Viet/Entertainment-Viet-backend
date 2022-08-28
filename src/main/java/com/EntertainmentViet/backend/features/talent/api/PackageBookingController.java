package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.PackageBookingBoundary;
import com.EntertainmentViet.backend.features.talent.dto.PackageBookingDto;
import com.EntertainmentViet.backend.features.talent.dto.PackageDto;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<Boolean> acceptBooking(@RequestBody @Valid PackageBookingDto packageBookingDto,
          @PathVariable("talent_uid") UUID talentId,
          @PathVariable("package_uid") UUID packageId) {
    return  CompletableFuture.completedFuture(packageBookingService.addPackageToShoppingCart(talentId, packageId, packageBookingDto));
  }

  @PostMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<UUID>> acceptBooking(
          @PathVariable("talent_uid") UUID talentId,
          @PathVariable("package_uid") UUID packageId,
          @PathVariable("uid") UUID bookingId) {
    return  CompletableFuture.completedFuture(packageBookingService.acceptBooking(talentId, packageId, bookingId)
            .map(newBookingInfo -> ResponseEntity
                    .ok()
                    .body(newBookingInfo)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<UUID>> rejectBooking(
          @PathVariable("talent_uid") UUID talentId,
          @PathVariable("package_uid") UUID packageId,
          @PathVariable("uid") UUID bookingId) {
    return  CompletableFuture.completedFuture(packageBookingService.rejectBooking(talentId, packageId, bookingId)
            .map(newBookingInfo -> ResponseEntity
                    .ok()
                    .body(newBookingInfo)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

}
