package com.EntertainmentViet.backend.features.scoresystem.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.PriorityScore;
import com.EntertainmentViet.backend.domain.entities.talent.ScoreType;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper(config = MappingConfig.class)
public abstract class ScoreTypeMapper {

  public abstract ScoreTypeDto toDto(ScoreType scoreType);

  public abstract ScoreType toModel(ScoreTypeDto dto);
}
