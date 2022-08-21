package com.EntertainmentViet.backend.features.talent.boundary;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;

import java.util.UUID;

public interface TalentBoundary {
    TalentDto findByUid(UUID uid);

    TalentDto create(TalentDto talentDto);

    TalentDto update(TalentDto talentDto, UUID uid) throws Exception;
}
