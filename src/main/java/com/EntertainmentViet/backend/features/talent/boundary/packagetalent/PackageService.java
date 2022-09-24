package com.EntertainmentViet.backend.features.talent.boundary.packagetalent;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.category.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import com.EntertainmentViet.backend.features.talent.dao.packagetalent.PackageRepository;
import com.EntertainmentViet.backend.features.talent.dao.talent.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PackageService implements PackageBoundary {

    private final PackageRepository packageRepository;

    private final PackageMapper packageMapper;

    private final JobDetailMapper jobDetailMapper;

    private final CategoryRepository categoryRepository;

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

        if (talentPackage == null) {
            log.warn(String.format("Can not find package with id '%s' ", uid));
            return Optional.empty();
        }

        if (!isPackageBelongToTalentWithUid(talentPackage, talentId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(packageMapper.toDto(talentPackage));
    }

    @Override
    public Optional<UUID> create(CreatePackageDto createPackageDto, UUID talentId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package aPackage = packageMapper.fromCreateDtoToModel(createPackageDto);
        aPackage.setTalent(talent);

        if (!isPackageValid(aPackage)) {
            return Optional.empty();
        }

        return Optional.ofNullable(packageRepository.save(aPackage).getUid());
    }

    @Override
    public Optional<UUID> update(UpdatePackageDto updatePackageDto, UUID talentId, UUID uid) {
        Package packageTalent = packageRepository.findByUid(uid).orElse(null);

        if (!isPackageWithUidExist(packageTalent, uid)) {
            return Optional.empty();
        }

        if (!isPackageBelongToTalentWithUid(packageTalent, talentId)) {
            return Optional.empty();
        }

        Package newPackage = packageMapper.fromUpdateDtoToModel(updatePackageDto);
        packageTalent.updateInfo(newPackage);
        return Optional.ofNullable(packageRepository.save(packageTalent).getUid());
    }

    @Override
    public boolean delete(UUID uid, UUID talentId) {
        Package talentPackage = packageRepository.findByUid(uid).orElse(null);

        if (talentPackage == null) {
            log.warn(String.format("Can not find package with id '%s' ", uid));
            return false;
        }

        if (!isPackageBelongToTalentWithUid(talentPackage, talentId)) {
            return false;
        }

        packageRepository.deleteById(talentPackage.getId());
        return true;
    }

    private boolean isPackageWithUidExist(Package packageTalent, UUID uid) {
        if (packageTalent == null) {
            log.warn(String.format("Can not find package with id '%s'", uid));
            return false;
        }
        return true;
    }

    private boolean isPackageValid(Package packageTalent) {
        if (packageTalent.getTalent() == null) {
            log.warn(String.format("Can not find talent owning the package with id '%s'", packageTalent.getUid()));
            return false;
        }
        if (packageTalent.getJobDetail() == null || packageTalent.getJobDetail().getCategory() == null) {
            log.warn(String.format("Can not populate jobDetail information for package with id '%s'", packageTalent.getUid()));
            return false;
        }
        return true;
    }

    private boolean isPackageBelongToTalentWithUid(Package packageTalent, UUID talentId) {
        Talent talent = packageTalent.getTalent();
        if (!talent.getUid().equals(talentId)) {
            log.warn(String.format("There is no talent with id '%s' have the package with id '%s'", talentId, packageTalent.getUid()));
            return false;
        }
        return true;
    }
}
