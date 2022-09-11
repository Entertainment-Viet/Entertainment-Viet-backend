package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {EventOpenPositionMapper.class, JobOfferMapper.class}, config = MappingConfig.class)
public abstract class EventMapper {

    //TODO Fix mapping
    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "organizer"})
    @Mapping(target = "organizerId", ignore = true)
    public abstract EventDto toDto(Event event);

    @BeanMapping(ignoreUnmappedSourceProperties = {"organizerId"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true) // TODO fix
    public abstract Event toModel(EventDto dto);
}
