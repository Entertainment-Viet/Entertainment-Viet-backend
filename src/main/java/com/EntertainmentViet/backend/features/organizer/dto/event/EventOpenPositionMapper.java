package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(uses = {
    JobOfferMapper.class,
},
    config = MappingConfig.class)
public abstract class EventOpenPositionMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "applicants"})
    @Mapping(target = "event", source = "event", qualifiedByName = "toEventUid")
    public abstract ReadEventOpenPositionDto toDto(EventOpenPosition eventOpenPosition);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicants", ignore = true)
    @Mapping(target = "event", ignore = true)
    public abstract EventOpenPosition fromCreateDtoToModel(CreateEventOpenPositionDto dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicants", ignore = true)
    @Mapping(target = "event", ignore = true)
    public abstract EventOpenPosition fromUpdateDtoToModel(UpdateEventOpenPositionDto dto);

    @Named("toEventUid")
    public UUID toEventUid(Event event) {
        return event != null ? event.getUid() : null;
    }
}
