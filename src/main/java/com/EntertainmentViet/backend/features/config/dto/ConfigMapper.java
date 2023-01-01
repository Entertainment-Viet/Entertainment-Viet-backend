package com.EntertainmentViet.backend.features.config.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.config.AppConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class ConfigMapper {

  @Mapping(target = "id", ignore = true)
  public abstract AppConfig toModel(ConfigDto configDto);

  @BeanMapping(ignoreUnmappedSourceProperties = { "id" })
  public abstract ConfigDto toDto(AppConfig appConfig);
}
