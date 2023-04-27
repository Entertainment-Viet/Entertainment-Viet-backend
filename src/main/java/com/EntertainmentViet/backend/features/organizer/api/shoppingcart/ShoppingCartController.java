package com.EntertainmentViet.backend.features.organizer.api.shoppingcart;

import com.EntertainmentViet.backend.exception.rest.UnauthorizedTokenException;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.shoppingcart.ShoppingCartBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ChargeCartItemDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ListCartItemParamDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.ReadCartItemDto;
import com.EntertainmentViet.backend.features.organizer.dto.shoppingcart.UpdateCartItemDto;
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

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadCartItemDto>>> findByOrganizerUid(JwtAuthenticationToken token,
                                                                                           @PathVariable("organizer_uid") UUID organizerUid,
                                                                                           @ParameterObject Pageable pageable,
                                                                                           @ParameterObject ListCartItemParamDto paramDto) {

    if (!organizerUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
    }

    return CompletableFuture.completedFuture(ResponseEntity.ok().body(RestUtils.toPageResponse(
        shoppingCartService.findByOrganizerUid(organizerUid, paramDto, pageable)
    )));
  }

  @PostMapping
  public CompletableFuture<ResponseEntity<Void>> charge(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("organizer_uid") UUID organizerUid,
                                                        @RequestBody @Valid ChargeCartItemDto chargeCartItemDto) {

    if (!organizerUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
    }

    if (shoppingCartService.charge(organizerUid, chargeCartItemDto)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @DeleteMapping
  public CompletableFuture<ResponseEntity<Void>> clear(JwtAuthenticationToken token, HttpServletRequest request,
                                                       @PathVariable("organizer_uid") UUID organizerUid) {
    if (!organizerUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
    }

    if (shoppingCartService.clear(organizerUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadCartItemDto>> findByUid(JwtAuthenticationToken token, HttpServletRequest request,
                                                                      @PathVariable("organizer_uid") UUID organizerUid,
                                                                      @PathVariable("uid") UUID uid) {
    if (!organizerUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
    }

    return CompletableFuture.completedFuture(shoppingCartService.findByOrganizerUidAndUid(organizerUid, uid)
        .map(cartItemDto -> ResponseEntity
            .ok()
            .body(cartItemDto)
        )
        .orElse(ResponseEntity.notFound().build()));
  }

  @PutMapping(value = "/{uid}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @RequestBody @Valid UpdateCartItemDto updateCartItemDto,
                                                        @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {
    if (!organizerUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
    }

    return CompletableFuture.completedFuture(shoppingCartService.update(updateCartItemDto, organizerUid, uid)
        .map(newJobOfferUid -> ResponseEntity
            .ok()
            .body(newJobOfferUid)
        )
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> delete(JwtAuthenticationToken token, @PathVariable("organizer_uid") UUID organizerUid, @PathVariable("uid") UUID uid) {
    if (!organizerUid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to get information of organizer with uid '%s'", organizerUid));
    }

    if (shoppingCartService.delete(uid, organizerUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
