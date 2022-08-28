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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        if (talent != null) {
            return packageRepository.findByUid(uid).map(packageMapper::toDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UUID> create(PackageDto packageDto, UUID talentId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        JobDetail jobDetail = jobDetailMapper.toModel(packageDto.getJobDetail());
        Category category = categoryRepository.findByUid(jobDetail.getCategory().getUid()).orElse(null);

        if (jobDetail == null || category == null || talent == null) {
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
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package aPackage = packageRepository.findByUid(uid).orElse(null);
        if (talent != null && aPackage != null && aPackage.getTalent().getId() == talent.getId()) {
            JobDetail jobDetail = aPackage.getJobDetail();
            Category category = categoryRepository.findByUid(packageDto.getJobDetail().getCategory().getUid()).orElse(null);
            jobDetail.setCategory(category);

            JobDetail jobDetailUpdate = jobDetailMapper.toModel(packageDto.getJobDetail());
            jobDetail.setPrice(jobDetailUpdate.getPrice());
            jobDetail.setWorkType(jobDetailUpdate.getWorkType());
            jobDetail.setPerformanceTime(jobDetailUpdate.getPerformanceTime());
            jobDetail.setPerformanceDuration(jobDetailUpdate.getPerformanceDuration());
            jobDetail.setNote(jobDetailUpdate.getNote());
            jobDetail.setExtensions(jobDetailUpdate.getExtensions());

            aPackage.setJobDetail(jobDetail);
            aPackage.setName(packageDto.getName());
            aPackage.setIsActive(packageDto.getIsActive());
            var newPackage = packageRepository.save(aPackage);
            return Optional.ofNullable(newPackage.getUid());
        }
        return Optional.empty();
    }

    @Override
    public void delete(UUID uid, UUID talentId) {
        Talent talent = talentRepository.findByUid(talentId).orElse(null);
        Package aPackage = packageRepository.findByUid(uid).orElse(null);
        if (talent != null && aPackage != null && aPackage.getTalent().getId() == talent.getId()) {
            packageRepository.deleteById(packageRepository.findByUid(uid).orElse(null).getId());
        }
    }
}
