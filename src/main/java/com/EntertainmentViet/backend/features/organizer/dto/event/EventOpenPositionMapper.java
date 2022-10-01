package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    JobOfferMapper.class,
    BookingMapper.class
},
    config = MappingConfig.class)
public abstract class EventOpenPositionMapper {

    // TODO
    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "event", "jobOffer", "applicants"})
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "jobOffer", ignore = true)
    @Mapping(target = "applicants", ignore = true)
    public abstract ReadEventOpenPositionDto toDto(EventOpenPosition eventOpenPosition);

    // TODO
    @BeanMapping(ignoreUnmappedSourceProperties = {"event", "jobOffer", "applicants"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobOffer", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "applicants", ignore = true)
    public abstract EventOpenPosition toModel(ReadEventOpenPositionDto dto);
}
