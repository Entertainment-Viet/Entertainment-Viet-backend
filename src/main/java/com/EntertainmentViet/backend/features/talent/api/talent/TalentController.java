package com.EntertainmentViet.backend.features.talent.api.talent;

import com.EntertainmentViet.backend.exception.KeycloakUnauthorizedException;
import com.EntertainmentViet.backend.features.admin.boundary.UserBoundary;
import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.talent.TalentBoundary;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentKycInfoDto;
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
@RequestMapping(path = TalentController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
@Slf4j
public class TalentController {

  public static final String REQUEST_MAPPING_PATH = "/talents";

  public static final String CONFIDENTIAL_PATH = "/confidential";

  public static final String CASH_PATH = "/cash";

  private final TalentBoundary talentService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadTalentDto>>> findAll(@ParameterObject Pageable pageable,
                                                                              @ParameterObject ListTalentParamDto paramDto) {
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(talentService.findAll(paramDto, pageable)));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadTalentDto>> findByUid(@PathVariable("uid") UUID uid) {

    return CompletableFuture.completedFuture(talentService.findByUid(uid)
            .map( talentDto -> ResponseEntity
                    .ok()
                    .body(talentDto)
            )
            .orElse( ResponseEntity.notFound().build()));
  }

  @PostMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> sendVerifyRequest(JwtAuthenticationToken token, @PathVariable("uid") UUID uid) {

    if (!uid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", uid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (talentService.sendVerifyRequest(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    log.warn(String.format("Can not verify talent account with id '%s'", uid));
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("uid") UUID uid, @RequestBody @Valid UpdateTalentDto updateTalentDto) {

    if (!uid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", uid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(talentService.update(updateTalentDto, uid)
            .map(updatedTalentUid -> ResponseEntity
                    .ok()
                    .body(updatedTalentUid)
            ).orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(value = "/{uid}" + CONFIDENTIAL_PATH)
  public CompletableFuture<ResponseEntity<UUID>> updateKyc(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @PathVariable("uid") UUID uid, @RequestBody @Valid UpdateTalentKycInfoDto kycInfoDto) {

    if (!uid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", uid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(talentService.updateKyc(kycInfoDto, uid)
        .map(updatedTalentUid -> ResponseEntity
            .ok()
            .body(updatedTalentUid)
        ).orElse(ResponseEntity.badRequest().build())
    );
  }
}
