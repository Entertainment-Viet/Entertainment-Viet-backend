package com.EntertainmentViet.backend.features.scoresystem.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.ScoreType;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class ScoreTypeMapper {

  public abstract ScoreTypeDto toDto(ScoreType scoreType);

  public abstract ScoreType toModel(ScoreTypeDto dto);
}
