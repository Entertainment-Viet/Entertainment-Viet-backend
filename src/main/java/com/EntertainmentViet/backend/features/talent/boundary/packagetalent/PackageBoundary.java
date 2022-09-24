package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.features.talent.dto.packagetalent.CreatePackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ReadPackageDto;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.UpdatePackageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PackageBoundary {

    Page<ReadPackageDto> findByTalentUid(UUID talentId, ListPackageParamDto paramDto, Pageable pageable);

    Optional<ReadPackageDto> findByUid(UUID talentId, UUID uid);

    Optional<UUID> create(CreatePackageDto createPackageDto, UUID talentId);

    Optional<UUID> update(UpdatePackageDto updatePackageDto, UUID talentId, UUID uid);

    boolean delete(UUID uid, UUID talentId);
}
