package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ReadPackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.UpdatePackageDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageBoundary {

    List<ReadPackageDto> findByTalentUid(UUID talentId);

    Optional<ReadPackageDto> findByUid(UUID talentId, UUID uid);

    Optional<UUID> create(CreatePackageDto createPackageDto, UUID talentId);

    Optional<UUID> update(UpdatePackageDto updatePackageDto, UUID talentId, UUID uid);

    boolean delete(UUID uid, UUID talentId);
}
