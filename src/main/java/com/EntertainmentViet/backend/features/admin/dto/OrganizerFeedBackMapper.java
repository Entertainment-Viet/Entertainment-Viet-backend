package com.EntertainmentViet.backend.features.admin.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class OrganizerFeedBackMapper {

    // TODO fix mapping
    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "uid", "createdAt", "organizer", "status", "admin", "content"})
    public abstract OrganizerFeedBackDto toDto(OrganizerFeedback talentFeedback);

    // TODO fix mapping
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "content", ignore = true)
    public abstract OrganizerFeedback toModel(OrganizerFeedBackDto dto);
}
