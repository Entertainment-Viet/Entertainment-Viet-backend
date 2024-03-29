package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.talent.dto.talent.*;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TalentBoundary {

    CustomPage<ReadTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable);

    Optional<ReadTalentDto> findByUid(UUID uid, boolean isOwnerUser);

    Optional<UUID> create(CreatedTalentDto createTalentDto, UUID uid);

    Optional<UUID> update(UpdateTalentDto updateTalentDto, UUID uid);

    Optional<UUID> updateKyc(UpdateTalentKycInfoDto kycInfoDto, UUID uid);

    boolean sendVerifyRequest(UUID uid);

    boolean verify(UUID uid);
}
