package com.EntertainmentViet.backend.features.admin.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.admin.TalentFeedback;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class TalentFeedBackMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "createdAt", "talent", "status", "admin", "content"})
    public abstract TalentFeedBackDto toDto(TalentFeedback talentFeedback);
}
