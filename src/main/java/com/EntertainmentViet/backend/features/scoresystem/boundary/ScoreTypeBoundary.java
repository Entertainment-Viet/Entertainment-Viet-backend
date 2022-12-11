package com.EntertainmentViet.backend.features.scoresystem.boundary;

import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreTypeDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScoreTypeBoundary {

    List<ScoreTypeDto> findAll(UUID id);

    Optional<Long> create(UUID id, ScoreTypeDto scoreTypeDto);
}
