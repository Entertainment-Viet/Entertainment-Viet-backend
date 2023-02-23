package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.features.admin.boundary.bookings.AdminBookingBoundary;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingParamDto;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminUpdateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.security.roles.AdminRole;
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

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = AdminBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminBookingController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/bookings";

  private final AdminBookingBoundary adminBookingService;

  @GetMapping
  public CompletableFuture<ResponseEntity<AdminListBookingResponseDto>> listBooking(@PathVariable("admin_uid") UUID adminUid,
                                                                                    @ParameterObject Pageable pageable,
                                                                                    @ParameterObject AdminListBookingParamDto paramDto) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        adminBookingService.listBooking(paramDto, pageable)
    ));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadBookingDto>> findByUid(@PathVariable("admin_uid") UUID adminUid, @PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(adminBookingService.findByUid(uid)
            .map(bookingDto -> ResponseEntity
                    .ok()
                    .body(bookingDto)
            ).orElse(ResponseEntity.notFound().build()));
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @PathVariable("uid") UUID uid,
                                                        @PathVariable("admin_uid") UUID adminUid,
                                                        @RequestBody @Valid AdminUpdateBookingDto updateBookingDto) {

    if (!adminUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information with uid '%s'", adminUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (!SecurityUtils.hasRole(AdminRole.ADMIN_UPDATE_BOOKING_PROOF.name())) {
      updateBookingDto.setFinishProof(null);
    }
    if (!SecurityUtils.hasRole(AdminRole.ADMIN_UPDATE_BOOKING_PAID.name())) {
      updateBookingDto.setIsPaid(null);
    }

    return  CompletableFuture.completedFuture(adminBookingService.update(adminUid, uid, updateBookingDto)
            .map(newBookingDto -> ResponseEntity
                    .ok()
                    .body(newBookingDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }
}
