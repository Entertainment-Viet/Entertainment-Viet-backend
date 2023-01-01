package com.EntertainmentViet.backend.features.talent.api.packagetalent;

import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.AddCartItemDto;
import com.EntertainmentViet.backend.features.talent.boundary.packagetalent.PackageBookingBoundary;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = PackageBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class PackageBookingController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages/{package_uid}/bookings";

  public static final String CART_PATH = "/shoppingcart";

  private final PackageBookingBoundary packageBookingService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadBookingDto>>> listBooking(JwtAuthenticationToken token,
                                                                                   @PathVariable("talent_uid") UUID talentUid,
                                                                                   @PathVariable("package_uid") UUID packageUid,
                                                                                   @ParameterObject Pageable pageable) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(RestUtils.toPageResponse(
        RestUtils.getPageEntity(packageBookingService.listBooking(talentUid, packageUid), pageable)
    )));
  }

  @PostMapping(value = PackageBookingController.CART_PATH,
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<Void>> addPackageToShoppingCart(JwtAuthenticationToken token, 
                                                                          @RequestBody @Valid AddCartItemDto addCartItemDto,
                                                                          @PathVariable("talent_uid") UUID talentUid,
                                                                          @PathVariable("package_uid") UUID packageUid) {

    if (packageBookingService.addPackageToShoppingCart(talentUid, packageUid, addCartItemDto)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> orderPackage(JwtAuthenticationToken token, HttpServletRequest request,
                                                              @PathVariable("talent_uid") UUID talentUid,
                                                              @PathVariable("package_uid") UUID packageUid,
                                                              @RequestBody @Valid CreatePackageOrderDto createPackageOrderDto) {
    return CompletableFuture.completedFuture(packageBookingService.create(talentUid, packageUid, createPackageOrderDto)
        .map(newBookingUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newBookingUid))
            .body(newBookingUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid,
                                                               @PathVariable("package_uid") UUID packageUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    if (packageBookingService.acceptBooking(talentUid, packageUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid,
                                                               @PathVariable("package_uid") UUID packageUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    if (packageBookingService.rejectBooking(talentUid, packageUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

}
