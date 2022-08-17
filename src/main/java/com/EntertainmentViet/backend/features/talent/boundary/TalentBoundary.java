package com.EntertainmentViet.backend.features.talent.boundary;
import com.EntertainmentViet.backend.features.talent.dto.TalentDto;

import java.util.List;
import java.util.UUID;

public interface TalentBoundary {

    List<TalentDto> findAll();

    TalentDto findByUid(UUID uid);
}
