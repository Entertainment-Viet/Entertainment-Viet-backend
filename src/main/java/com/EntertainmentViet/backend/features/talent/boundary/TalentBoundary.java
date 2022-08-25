package com.EntertainmentViet.backend.features.talent.boundary;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;

import java.util.Optional;
import java.util.UUID;

public interface TalentBoundary {
    Optional<TalentDto> findByUid(UUID uid);

    Optional<UUID> create(TalentDto talentDto);

    Optional<UUID> update(TalentDto talentDto, UUID uid);
}
