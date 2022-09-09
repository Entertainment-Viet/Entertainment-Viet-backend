package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.PackageBookingBoundary;
import com.EntertainmentViet.backend.features.talent.dto.CreatePackageBookingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = PackageBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class PackageBookingController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages/{package_uid}/bookings";
  private final PackageBookingBoundary packageBookingService;

  @GetMapping()
  public CompletableFuture<ResponseEntity<List<ReadBookingDto>>> listBooking(JwtAuthenticationToken token,
                                                                             @PathVariable("talent_uid") UUID talentUid,
                                                                             @PathVariable("package_uid") UUID packageUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token))) {
      log.warn(String.format("The token don't have enough access right to get information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(packageBookingService.listBooking(talentUid, packageUid)));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<Void>> addPackageToShoppingCart(JwtAuthenticationToken token, 
                                                                          @RequestBody @Valid CreatePackageBookingDto createPackageBookingDto,
                                                                          @PathVariable("talent_uid") UUID talentUid,
                                                                          @PathVariable("package_uid") UUID packageUid) {

    if (!createPackageBookingDto.getOrganizerId().equals(RestUtils.getUidFromToken(token))) {
      log.warn(String.format("The token don't have enough access right to proceed with this api. Require update permission to organizer with id '%s'", createPackageBookingDto.getOrganizerId()));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    if (packageBookingService.addPackageToShoppingCart(talentUid, packageUid, createPackageBookingDto)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid, 
                                                               @PathVariable("package_uid") UUID packageUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token))) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    if (packageBookingService.acceptBooking(talentUid, packageUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid,
                                                               @PathVariable("package_uid") UUID packageUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token))) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    if (packageBookingService.rejectBooking(talentUid, packageUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

}
