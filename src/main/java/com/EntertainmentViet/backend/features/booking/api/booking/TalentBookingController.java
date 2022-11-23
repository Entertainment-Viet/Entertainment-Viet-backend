package com.EntertainmentViet.backend.features.booking.api.booking;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.EntertainmentViet.backend.features.booking.boundary.booking.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.boundary.booking.TalentBookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.booking.CreateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListBookingResponseDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ListTalentBookingParamDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.ReadBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.booking.UpdateBookingDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.talent.ReviewBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = TalentBookingController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
@Slf4j
public class TalentBookingController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/bookings";

  public static final String DONE_PATH = "/done";

  private final BookingBoundary bookingService;

  private final TalentBookingBoundary talentBookingService;

  private final ReviewBoundary reviewService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadBookingDto>> findByUid(JwtAuthenticationToken token,
                                                                     @PathVariable("talent_uid") UUID talentUid, @PathVariable("uid") UUID uid) {
    boolean isCurrentUser = talentUid.equals(RestUtils.getUidFromToken(token)) || RestUtils.isTokenContainPermissions(token, "ROOT");
    return CompletableFuture.completedFuture(bookingService.findByUid(isCurrentUser, talentUid, uid)
            .map(bookingDto -> ResponseEntity
                    .ok()
                    .body(bookingDto)
            ).orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("talent_uid") UUID talentUid,
                                                        @RequestBody @Valid CreateBookingDto createBookingDto) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(talentBookingService.create(talentUid, createBookingDto)
            .map(newBookingDto -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newBookingDto))
                    .body(newBookingDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @PathVariable("uid") UUID uid,
                                                        @PathVariable("talent_uid") UUID talentUid,
                                                        @RequestBody @Valid UpdateBookingDto updateBookingDto) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(talentBookingService.update(talentUid, uid, updateBookingDto)
            .map(newBookingDto -> ResponseEntity
                    .ok()
                    .body(newBookingDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @GetMapping
  public CompletableFuture<ResponseEntity<ListBookingResponseDto>> listBooking(JwtAuthenticationToken token,
                                                                               @PathVariable("talent_uid") UUID talentUid,
                                                                               @ParameterObject Pageable pageable,
                                                                               @ParameterObject ListTalentBookingParamDto paramDto) {
    boolean isCurrentUser = talentUid.equals(RestUtils.getUidFromToken(token)) || RestUtils.isTokenContainPermissions(token, "ROOT");
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        talentBookingService.listBooking(isCurrentUser, talentUid, paramDto, pageable)
    ));
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (talentBookingService.acceptBooking(talentUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> rejectBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (talentBookingService.rejectBooking(talentUid, bookingUid)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PostMapping(value = "/{uid}" + DONE_PATH)
  public CompletableFuture<ResponseEntity<Void>> finishBookingAndAddReview(JwtAuthenticationToken token, HttpServletRequest request,
                                                                           @PathVariable("talent_uid") UUID talentUid,
                                                                           @PathVariable("uid") UUID bookingUid,
                                                                           @RequestBody @Valid CreateReviewDto reviewDto) {
    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (talentBookingService.finishBooking(talentUid, bookingUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
