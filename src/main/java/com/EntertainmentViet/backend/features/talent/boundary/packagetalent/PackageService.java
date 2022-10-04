package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.common.utils.EntityValidationUtils;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.packagetalent.PackageRepository;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PackageService implements PackageBoundary {

    private final PackageRepository packageRepository;

    private final PackageMapper packageMapper;

    private final TalentRepository talentRepository;

    @Override
    public Page<ReadPackageDto> findByTalentUid(UUID talentId, ListPackageParamDto paramDto, Pageable pageable) {
        var dtoList = packageRepository.findByTalentUid(talentId, paramDto, pageable).stream()
            .map(packageMapper::toDto)
            .collect(Collectors.toList());

        return RestUtils.getPageEntity(dtoList, pageable);
    }

    @Override
    public Optional<ReadPackageDto> findByUid(UUID talentId, UUID uid) {
        Package talentPackage = packageRepository.findByUid(uid).orElse(null);

        if (!EntityValidationUtils.isPackageWithUidExist(talentPackage, uid)) {
            return Optional.empty();
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(talentPackage, talentId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(packageMapper.toDto(talentPackage));
    }

    @Override
    public Optional<UUID> create(CreatePackageDto createPackageDto, UUID talentId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package aPackage = packageMapper.fromCreateDtoToModel(createPackageDto);
        aPackage.setTalent(talent);

        if (aPackage.getTalent() == null) {
            log.warn(String.format("Can not find talent owning the package with id '%s'", aPackage.getUid()));
            return Optional.empty();
        }
        if (aPackage.getJobDetail() == null || aPackage.getJobDetail().getCategory() == null) {
            log.warn(String.format("Can not populate jobDetail information for package with id '%s'", aPackage.getUid()));
            return Optional.empty();
        }

        return Optional.ofNullable(packageRepository.save(aPackage).getUid());
    }

    @Override
    public Optional<UUID> update(UpdatePackageDto updatePackageDto, UUID talentId, UUID uid) {
        Package packageTalent = packageRepository.findByUid(uid).orElse(null);

        if (!EntityValidationUtils.isPackageWithUidExist(packageTalent, uid)) {
            return Optional.empty();
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return Optional.empty();
        }

        Package newPackage = packageMapper.fromUpdateDtoToModel(updatePackageDto);
        packageTalent.updateInfo(newPackage);
        return Optional.ofNullable(packageRepository.save(packageTalent).getUid());
    }

    @Override
    public boolean delete(UUID uid, UUID talentId) {
        Package talentPackage = packageRepository.findByUid(uid).orElse(null);

        if (!EntityValidationUtils.isPackageWithUidExist(talentPackage, uid)) {
            return false;
        }

        if (!EntityValidationUtils.isPackageBelongToTalentWithUid(talentPackage, talentId)) {
            return false;
        }

        packageRepository.deleteById(talentPackage.getId());
        return true;
    }
}
