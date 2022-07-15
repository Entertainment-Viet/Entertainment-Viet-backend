package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.Organizer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class OrganizerMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
  public abstract OrganizerDto toDto(Organizer organizer);

  @Mapping(target = "id", ignore = true)
  public abstract Organizer toModel(OrganizerDto organizerDto);
}
