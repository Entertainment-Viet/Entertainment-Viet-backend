package com.EntertainmentViet.backend.features.organizer.api.shoppingcart;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.joboffer.JobOfferBoundary;
import com.EntertainmentViet.backend.features.organizer.boundary.shoppingcart.ShoppingCartBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.CreateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.ReadJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.UpdateJobOfferDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ChargeCartItemDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ReadCartItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@Async
@Validated
@RequestMapping(path = ShoppingCartController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController {

  public static final String REQUEST_MAPPING_PATH = "/organizers/{organizer_uid}/shoppingcart";

  private final ShoppingCartBoundary shoppingCartService;

  @GetMapping()
  public CompletableFuture<ResponseEntity<List<ReadCartItemDto>>> findByOrganizerUid(JwtAuthenticationToken token,
                                                                                     @PathVariable("organizer_uid") UUID organizerUid) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(shoppingCartService.findByOrganizerUid(organizerUid)));
  }

  @PostMapping()
  public CompletableFuture<ResponseEntity<Void>> charge(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @RequestBody @Valid ChargeCartItemDto chargeCartItemDto) {

    if (!organizerUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (shoppingCartService.charge(organizerUid, chargeCartItemDto)) {
      return  CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

}
