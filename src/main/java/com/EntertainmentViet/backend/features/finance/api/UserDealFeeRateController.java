package com.EntertainmentViet.backend.features.finance.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.finance.boundary.UserDealFeeRateBoundary;
import com.EntertainmentViet.backend.features.finance.dto.UserDealFeeRateDto;
import com.EntertainmentViet.backend.features.finance.dto.UserDealFeeRateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = UserDealFeeRateController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
@Slf4j
public class UserDealFeeRateController {

  public static final String REQUEST_MAPPING_PATH = "/users/{user_uid}/fee";

  private final UserDealFeeRateBoundary userDealFeeRateService;

  private final UserDealFeeRateMapper userDealFeeRateMapper;

  @GetMapping
  public CompletableFuture<ResponseEntity<UserDealFeeRateDto>> findByUid(JwtAuthenticationToken token, @PathVariable("user_uid") UUID userUid) {
    if (!userUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update fee for user with uid '%s'", userUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return CompletableFuture.completedFuture(userDealFeeRateService.findByUid(userUid)
        .map(userDealFeeRate -> ResponseEntity
            .ok()
            .body(userDealFeeRateMapper.toReadDto(userDealFeeRate))
        ).orElse(ResponseEntity.notFound().build()));
  }

  @PostMapping
  public CompletableFuture<ResponseEntity<UserDealFeeRateDto>> update(@PathVariable("user_uid") UUID userUid,  @RequestBody @Valid UserDealFeeRateDto updatedFeeRate) {
    return CompletableFuture.completedFuture(userDealFeeRateService.update(userUid, updatedFeeRate)
        .map(userDealFeeRate -> ResponseEntity
            .ok()
            .body(userDealFeeRateMapper.toReadDto(userDealFeeRate))
        ).orElse(ResponseEntity.badRequest().build()));
  }
}
