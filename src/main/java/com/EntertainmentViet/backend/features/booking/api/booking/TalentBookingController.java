package com.EntertainmentViet.backend.features.booking.api.booking;

import com.EntertainmentViet.backend.exception.rest.UnauthorizedTokenException;
import com.EntertainmentViet.backend.features.booking.boundary.booking.BookingBoundary;
import com.EntertainmentViet.backend.features.booking.boundary.booking.TalentBookingBoundary;
import com.EntertainmentViet.backend.features.booking.dto.booking.*;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.talent.boundary.talent.ReviewBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.CreateReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
  public CompletableFuture<ResponseEntity<DetailBookingResponseDto>> findByUid(JwtAuthenticationToken token,
                                                                               @PathVariable("talent_uid") UUID talentUid, @PathVariable("uid") UUID uid) {
    boolean isOwnerUser = talentUid.equals(TokenUtils.getUid(token)) || TokenUtils.isTokenContainPermissions(token, "ROOT");
    return CompletableFuture.completedFuture(bookingService.findByUid(isOwnerUser, talentUid, uid)
            .map(bookingDto -> ResponseEntity
                    .ok()
                    .body(bookingDto)
            ).orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<List<UUID>>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                              @PathVariable("talent_uid") UUID talentUid,
                                                              @RequestBody @Valid CreateBookingDto createBookingDto) {

    if (!talentUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
    }

    return  CompletableFuture.completedFuture(talentBookingService.create(talentUid, createBookingDto)
            .map(newBookingUidList -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newBookingUidList.get(0)))
                    .body(newBookingUidList)
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

    if (!talentUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
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
    boolean isOwnerUser = talentUid.equals(TokenUtils.getUid(token)) || TokenUtils.isTokenContainPermissions(token, "ROOT");
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(
        talentBookingService.listBooking(isOwnerUser, talentUid, paramDto, pageable)
    ));
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> acceptBooking(JwtAuthenticationToken token,
                                                               @PathVariable("talent_uid") UUID talentUid,
                                                               @PathVariable("uid") UUID bookingUid) {

    if (!talentUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
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

    if (!talentUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
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
    if (!talentUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
    }

    if (talentBookingService.finishBooking(talentUid, bookingUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
