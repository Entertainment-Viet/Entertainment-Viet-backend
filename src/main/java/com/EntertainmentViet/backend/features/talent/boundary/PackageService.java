package com.EntertainmentViet.backend.features.talent.boundary;

import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import com.EntertainmentViet.backend.features.talent.dao.PackageRepository;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import com.EntertainmentViet.backend.features.talent.dto.PackageDto;
import com.EntertainmentViet.backend.features.talent.dto.PackageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<PackageDto> findByTalentUid(UUID talentId) {
        return packageRepository.findByTalentUid(talentId).stream().map(packageMapper::toDto).toList();
    }

    @Override
    public Optional<PackageDto> findByUid(UUID talentId, UUID uid) {
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
    public Optional<UUID> create(PackageDto packageDto, UUID talentId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(packageDto.getJobDetail());
        Category category = categoryRepository.findByUid(jobDetail.getCategory().getUid()).orElse(null);

        if (jobDetail == null || category == null || talent == null) {
            log.warn("Can not find required information to create new package ");
            Optional.empty();
        }

        jobDetail.setCategory(category);
        Package aPackage = packageMapper.toModel(packageDto);
        aPackage.setTalent(talent);
        aPackage.setJobDetail(jobDetail);
        aPackage.setIsActive(true);
        aPackage.setId(null);
        aPackage.setUid(null);

        var newPackage = packageRepository.save(aPackage);
        return Optional.ofNullable(newPackage.getUid());
    }

    @Override
    public Optional<UUID> update(PackageDto packageDto, UUID talentId, UUID uid) {
        Package talentPackage = packageRepository.findByUid(uid).orElse(null);

        if (talentPackage == null) {
            log.warn(String.format("Can not find package with id '%s' ", uid));
            return Optional.empty();
        }

        if (!isPackageBelongToTalentWithUid(talentPackage, talentId)) {
            return Optional.empty();
        }

        JobDetail jobDetail = talentPackage.getJobDetail();
        Category category = categoryRepository.findByUid(packageDto.getJobDetail().getCategory().getUid()).orElse(null);
        jobDetail.setCategory(category);

        JobDetail jobDetailUpdate = jobDetailMapper.toModel(packageDto.getJobDetail());
        jobDetail.setPrice(jobDetailUpdate.getPrice());
        jobDetail.setWorkType(jobDetailUpdate.getWorkType());
        jobDetail.setPerformanceTime(jobDetailUpdate.getPerformanceTime());
        jobDetail.setPerformanceDuration(jobDetailUpdate.getPerformanceDuration());
        jobDetail.setNote(jobDetailUpdate.getNote());
        jobDetail.setExtensions(jobDetailUpdate.getExtensions());
        jobDetail.setLocation(jobDetailUpdate.getLocation());

        talentPackage.setJobDetail(jobDetail);
        talentPackage.setName(packageDto.getName());
        talentPackage.setIsActive(packageDto.getIsActive());
        var newPackage = packageRepository.save(talentPackage);
        return Optional.ofNullable(newPackage.getUid());
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

    private boolean isPackageBelongToTalentWithUid(Package packageTalent, UUID talentId) {
        if (packageTalent.getTalent() == null) {
            log.warn(String.format("Can not find talent owning the package with id '%s'", packageTalent.getUid()));
            return false;
        }

        Talent talent = packageTalent.getTalent();
        if (!talent.getUid().equals(talentId)) {
            log.warn(String.format("There is no talent with id '%s' have the package with id '%s'", talentId, packageTalent.getUid()));
            return false;
        }
        return true;
    }
}
