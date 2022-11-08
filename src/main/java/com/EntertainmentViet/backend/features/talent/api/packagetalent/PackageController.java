package com.EntertainmentViet.backend.features.talent.api.packagetalent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.common.utils.QueryParamsUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.packagetalent.PackageBoundary;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ReadPackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.UpdatePackageDto;
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
@RequestMapping(path = PackageController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
@Slf4j
public class PackageController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages";

  private final PackageBoundary packageService;

  @GetMapping
  public CompletableFuture<ResponseEntity<CustomPage<ReadPackageDto>>> findAll(@PathVariable("talent_uid") UUID talentUid,
                                                                               @ParameterObject Pageable pageable,
                                                                               @ParameterObject ListPackageParamDto paramDto) {
    if (QueryParamsUtils.isInvalidParams(paramDto)) {
      log.warn(String.format("Currency is not provided '%s'", paramDto.getCurrency()));
      return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.ok().body(RestUtils.toPageResponse(
        packageService.findByTalentUid(talentUid, paramDto, pageable)
    )));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadPackageDto>> findByUid(@PathVariable("talent_uid") UUID talentUid, @PathVariable("uid") UUID uid) {

    return CompletableFuture.completedFuture(packageService.findByUid(talentUid, uid)
            .map( packageDto -> ResponseEntity
                    .ok()
                    .body(packageDto)
            )
            .orElse( ResponseEntity.notFound().build()));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<UUID>> create(JwtAuthenticationToken token, HttpServletRequest request,
                                                        @RequestBody @Valid CreatePackageDto createPackageDto,
                                                        @PathVariable("talent_uid") UUID talentUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(packageService.create(createPackageDto, talentUid)
            .map(newPackageUid -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newPackageUid))
                    .body(newPackageUid)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  public CompletableFuture<ResponseEntity<UUID>> update(JwtAuthenticationToken token, @RequestBody @Valid UpdatePackageDto updatePackageDto,
                                                        @PathVariable("talent_uid") UUID talentUid, @PathVariable("uid") UUID uid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    return  CompletableFuture.completedFuture(packageService.update(updatePackageDto, talentUid, uid)
            .map(newPackageDto -> ResponseEntity
                    .ok()
                    .body(newPackageDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<HttpStatus>> delete(JwtAuthenticationToken token,
                                           @PathVariable("uid") UUID uid, @PathVariable("talent_uid") UUID talentUid) {

    if (!talentUid.equals(RestUtils.getUidFromToken(token)) && !RestUtils.isTokenContainPermissions(token, "ROOT")) {
      log.warn(String.format("The token don't have enough access right to update information of talent with uid '%s'", talentUid));
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    if (packageService.delete(uid, talentUid)) {
      return CompletableFuture.completedFuture(ResponseEntity.accepted().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
