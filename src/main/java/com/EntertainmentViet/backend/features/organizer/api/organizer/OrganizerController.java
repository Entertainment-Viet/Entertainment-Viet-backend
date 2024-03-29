package com.EntertainmentViet.backend.features.organizer.api.organizer;

import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.EntertainmentViet.backend.exception.rest.UnauthorizedTokenException;
import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import com.EntertainmentViet.backend.features.organizer.boundary.organizer.OrganizerBoundary;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.ReadOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerDto;
import com.EntertainmentViet.backend.features.organizer.dto.organizer.UpdateOrganizerKycInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = OrganizerController.REQUEST_MAPPING_PATH)
@Slf4j
public class OrganizerController {

  public static final String REQUEST_MAPPING_PATH = "/organizers";

  public static final String CONFIDENTIAL_PATH = "/confidential";

  public static final String CASH_PATH = "/cash";

  private final OrganizerBoundary organizerService;

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadOrganizerDto>> findByUid(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {
    boolean isOwnerUser = uid.equals(TokenUtils.getUid(token)) || TokenUtils.isTokenContainPermissions(token,"ROOT");

    return CompletableFuture.completedFuture(organizerService.findByUid(uid, isOwnerUser)
        .map(organizerDto -> ResponseEntity
            .ok()
            .body(organizerDto)
        )
        .orElse(ResponseEntity.notFound().build())
    );
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> sendVerifyRequest(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {

    if (!uid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of organizer with uid '%s'", uid));
    }

    if (organizerService.sendVerifyRequest(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

    throw new InvalidInputException(String.format("Can not verify organizer account with id '%s'", uid));
  }

  @PutMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> updateById(JwtAuthenticationToken token,
                                                            @PathVariable("uid") UUID uid, @RequestBody @Valid UpdateOrganizerDto updateOrganizerDto) {

    if (!uid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of organizer with uid '%s'", uid));
    }

    return  CompletableFuture.completedFuture(organizerService.update(updateOrganizerDto, uid)
        .map(updatedOrganizerUid -> ResponseEntity
            .ok()
            .body(updatedOrganizerUid)
        ).orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(value = "/{uid}" + CONFIDENTIAL_PATH)
  public CompletableFuture<ResponseEntity<UUID>> updateKycInfo(JwtAuthenticationToken token,
                                                               @PathVariable("uid") UUID uid, @RequestBody @Valid UpdateOrganizerKycInfoDto kycInfoDto) {

    if (!uid.equals(TokenUtils.getUid(token)) && !TokenUtils.isTokenContainPermissions(token, "ROOT")) {
      throw new UnauthorizedTokenException(String.format("The token don't have enough access right to update information of organizer with uid '%s'", uid));
    }

    var resultUidOptional = organizerService.updateKyc(kycInfoDto, uid);

    if (resultUidOptional.isPresent() && organizerService.sendVerifyRequest(resultUidOptional.get())) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @GetMapping(value = "/{uid}" + CASH_PATH)
  public Long receivePayment(JwtAuthenticationToken token, @PathVariable("uid") Long uid) {
    // TODO
    return null;
  }

  @PostMapping(value = "/{uid}" + CASH_PATH)
  public Long proceedPayment(JwtAuthenticationToken token, @PathVariable("uid") Long uid) {
    // TODO
    return null;
  }

}
