package com.EntertainmentViet.backend.features.talent.boundary.talent;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import com.EntertainmentViet.backend.features.talent.dto.talent.ListTalentParamDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReadTalentDto;
import com.EntertainmentViet.backend.features.talent.dto.talent.UpdateTalentDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TalentBoundary {

    CustomPage<ReadTalentDto> findAll(ListTalentParamDto paramDto, Pageable pageable);

    Optional<ReadTalentDto> findByUid(UUID uid);

    Optional<UUID> create(UpdateTalentDto createTalentDto, UUID uid);

    Optional<UUID> update(UpdateTalentDto updateTalentDto, UUID uid);

    boolean verify(UUID uid);
}
