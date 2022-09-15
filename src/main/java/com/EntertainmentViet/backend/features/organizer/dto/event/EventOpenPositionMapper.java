package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {JobOfferMapper.class, BookingMapper.class}, config = MappingConfig.class)
public abstract class EventOpenPositionMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "event"})
    @Mapping(target = "eventId", ignore = true) // TODO fix this
    public abstract EventOpenPositionDto toDto(EventOpenPosition eventOpenPosition);

    @BeanMapping(ignoreUnmappedSourceProperties = {"eventId"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true) // TODO fix this
    public abstract EventOpenPosition toModel(EventOpenPositionDto dto);
}
