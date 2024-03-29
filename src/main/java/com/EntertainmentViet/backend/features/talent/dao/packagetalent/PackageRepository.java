package com.EntertainmentViet.backend.features.talent.dao.packagetalent;

import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.ListPackageParamDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface PackageRepository extends IdentifiableRepository<Package> {

    List<Package> findByTalentUid(UUID uid, ListPackageParamDto paramDto, Pageable pageable);
}
