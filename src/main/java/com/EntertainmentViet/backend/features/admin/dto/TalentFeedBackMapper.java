package com.EntertainmentViet.backend.features.admin.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.admin.TalentFeedback;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class TalentFeedBackMapper {

    // TODO
    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "createdAt", "talent", "status", "admin", "content"})
    public abstract TalentFeedBackDto toDto(TalentFeedback talentFeedback);

    // TODO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "content", ignore = true)
    public abstract TalentFeedback toModel(TalentFeedBackDto dto);
}
