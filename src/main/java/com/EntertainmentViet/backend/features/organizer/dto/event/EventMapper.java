package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    EventOpenPositionMapper.class,
    JobOfferMapper.class,
    EntityMapper.class,
}, config = MappingConfig.class)
public abstract class EventMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "openPositions"})
    @Mapping(target = "occurrenceAddress", source = "eventDetail.occurrenceAddress")
    @Mapping(target = "occurrenceTime", source = "eventDetail.occurrenceTime")
    @Mapping(target = "organizerId", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerUid.class)
    @Mapping(target = "organizerName", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerName.class)
    public abstract ReadEventDto toReadDto(Event event);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openPositions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "eventDetail.occurrenceAddress", source = "occurrenceAddress")
    @Mapping(target = "eventDetail.occurrenceTime", source = "occurrenceTime")
    public abstract Event fromCreateDtoToModel(CreateEventDto dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openPositions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "eventDetail.occurrenceAddress", source = "occurrenceAddress")
    @Mapping(target = "eventDetail.occurrenceTime", source = "occurrenceTime")
    public abstract Event fromUpdateDtoToModel(UpdateEventDto dto);
}
