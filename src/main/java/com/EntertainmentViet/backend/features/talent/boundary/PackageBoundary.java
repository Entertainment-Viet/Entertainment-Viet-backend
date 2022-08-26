package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.features.talent.dto.PackageDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageBoundary {

    List<PackageDto> findAll(UUID talentId);

    Optional<PackageDto> findByTalentUidAndUid(UUID talentId, UUID uid);

    Optional<UUID> create(PackageDto packageDto, UUID talentId);

    Optional<UUID> update(PackageDto packageDto, UUID talentId, UUID uid);

    void delete(UUID uid, UUID talentId);
}
