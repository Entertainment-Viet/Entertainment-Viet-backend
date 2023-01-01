package com.EntertainmentViet.backend.features.admin.api;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.admin.boundary.bookings.AdminBookingBoundary;
import com.EntertainmentViet.backend.features.admin.boundary.talent.AdminTalentBoundary;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminListBookingResponseDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.ReadAdminTalentDto;
import com.EntertainmentViet.backend.features.admin.dto.talent.UpdateAdminTalentDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.QueryParamsUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
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
@RequestMapping(path = AdminTalentController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminTalentController {

  public static final String REQUEST_MAPPING_PATH = "/admins/{admin_uid}/talents";
  public static final String BOOKING_PATH = "/bookings";
  public static final String DEACTIVE_PATH = "/deactive";

  private final AdminTalentBoundary adminTalentService;
  private final AdminBookingBoundary adminBookingService;
  private final UserBoundary userService;

  @GetMapping(value = "/{talent_uid}" + BOOKING_PATH)
  public CompletableFuture<ResponseEntity<AdminListBookingResponseDto>> listBooking(@PathVariable("talent_uid") UUID talentUid,
                                                                                    @ParameterObject Pageable pageable,
                                                                                    @ParameterObject ListTalentBookingParamDto paramDto) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        adminBookingService.listTalentBooking(talentUid, paramDto, pageable)
    ));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadAdminTalentDto>> findByUid(JwtAuthenticationToken token,
      @PathVariable("admin_uid") UUID adminUid,
      @PathVariable("uid") UUID uid) {

    if (!adminUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of admin with uid '%s'",
          adminUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(adminTalentService.findByUid(adminUid, uid)
        .map(talentDto -> ResponseEntity
            .ok()
            .body(talentDto))
        .orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> verify(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {

    try {
      if (userService.verifyTalent(uid)) {
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
      }
    } catch (KeycloakUnauthorizedException ex) {
      log.error("Can not verify talent account in keycloak server", ex);
    }
    log.warn(String.format("Can not verify talent account with id '%s'", uid));
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token,
      @PathVariable("admin_uid") UUID adminUid,
      @PathVariable("uid") UUID uid,
      @RequestBody @Valid UpdateAdminTalentDto updateAdminTalentDto) {

    if (!adminUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of admin with uid '%s'",
          adminUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(adminTalentService.update(updateAdminTalentDto, uid)
        .map(updatedTalent -> ResponseEntity
            .ok()
            .body(updatedTalent))
        .orElse(ResponseEntity.badRequest().build()));
  }

  @GetMapping()
  public CompletableFuture<ResponseEntity<CustomPage<ReadAdminTalentDto>>> findAll(
      @ParameterObject Pageable pageable,
      @ParameterObject ListTalentParamDto paramDto) {
    if (QueryParamsUtils.isInvalidParams(paramDto)) {
      log.warn(String.format("Currency is not provided '%s'", paramDto.getCurrency()));
      return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        adminTalentService.findAll(paramDto, pageable)));
  }

  @DeleteMapping(value = "/{uid}" + DEACTIVE_PATH)
  public CompletableFuture<ResponseEntity<Void>> delete(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {
    if (adminTalentService.delete(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> disapprove(JwtAuthenticationToken token,
      @PathVariable("uid") UUID uid) {
    if (adminTalentService.disapprove(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
