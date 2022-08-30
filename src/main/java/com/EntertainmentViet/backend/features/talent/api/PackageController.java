package com.EntertainmentViet.backend.features.talent.api;

import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.boundary.PackageBoundary;
import com.EntertainmentViet.backend.features.talent.dto.PackageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = PackageController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Async
@Validated
public class PackageController {

  public static final String REQUEST_MAPPING_PATH = "/talents/{talent_uid}/packages";

  private final PackageBoundary packageService;

  @GetMapping()
  public CompletableFuture<List<PackageDto>> findAll(@PathVariable("talent_uid") UUID talentId) {
    return CompletableFuture.completedFuture(packageService.findByTalentUid(talentId));
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<PackageDto>> findByUid(@PathVariable("talent_uid") UUID talentId,
                                                                           @PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(packageService.findByUid(talentId, uid)
            .map( packageDto -> ResponseEntity
                    .ok()
                    .body(packageDto)
            )
            .orElse( ResponseEntity.notFound().build()));
  }

  @PostMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @RequestBody @Valid PackageDto packageDto,
                                                        @PathVariable("talent_uid") UUID talentId) {
    return  CompletableFuture.completedFuture(packageService.create(packageDto, talentId)
            .map(newPackageDto -> ResponseEntity
                    .created(RestUtils.getCreatedLocationUri(request, newPackageDto))
                    .body(newPackageDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @PutMapping(
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE,
          value = "/{uid}")
  @ResponseStatus(HttpStatus.OK)
  public CompletableFuture<ResponseEntity<UUID>> update(@RequestBody @Valid PackageDto packageDto,
                                                        @PathVariable("talent_uid") UUID talentId, @PathVariable("uid") UUID uid) {
    return  CompletableFuture.completedFuture(packageService.update(packageDto, talentId, uid)
            .map(newPackageDto -> ResponseEntity
                    .ok()
                    .body(newPackageDto)
            )
            .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public ResponseEntity<HttpStatus> delete(@PathVariable("uid") UUID uid, @PathVariable("talent_uid") UUID talentId) {
    if (packageService.delete(uid, talentId)) {
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
