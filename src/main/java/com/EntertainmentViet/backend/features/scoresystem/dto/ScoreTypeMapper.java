package com.EntertainmentViet.backend.features.scoresystem.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.ScoreType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class ScoreTypeMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
  public abstract ScoreTypeDto toDto(ScoreType scoreType);

  @BeanMapping(ignoreUnmappedSourceProperties = {"uid"})
  @Mapping(target = "id", ignore = true)
  public abstract ScoreType toModel(ScoreTypeDto dto);
}
