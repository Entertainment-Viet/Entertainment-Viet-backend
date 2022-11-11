package com.EntertainmentViet.backend.features.organizer.dto.event;

import java.util.List;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressMapper;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
        EventOpenPositionMapper.class,
        JobOfferMapper.class,
        EntityMapper.class,
        UserInputTextMapper.class,
        LocationAddressMapper.class,
}, config = MappingConfig.class)
public abstract class EventMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = { "id" })
    @Mapping(target = "occurrenceAddress", source = "eventDetail.occurrenceAddress")
    @Mapping(target = "occurrenceStartTime", source = "eventDetail.occurrenceStartTime")
    @Mapping(target = "occurrenceEndTime", source = "eventDetail.occurrenceEndTime")
    @Mapping(target = "legalPaper", source = "eventDetail.legalPaper")
    @Mapping(target = "openPositionsCount", source = "openPositions", qualifiedByName = "toEventOpenPositions")
    @Mapping(target = "description", source = "eventDetail.description", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "organizerId", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerUid.class)
    @Mapping(target = "organizerName", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerName.class)
    public abstract ReadEventDto toReadDto(Event event);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openPositions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "eventDetail.occurrenceAddress", source = "occurrenceAddress",
            qualifiedBy = LocationAddressMapper.ToLocationAddress.class)
    @Mapping(target = "eventDetail.occurrenceEndTime", source = "occurrenceEndTime")
    @Mapping(target = "eventDetail.occurrenceStartTime", source = "occurrenceStartTime")
    @Mapping(target = "eventDetail.legalPaper", source = "legalPaper")
    @Mapping(target = "eventDetail.description", source = "description", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Event fromCreateDtoToModel(CreateEventDto dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openPositions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "eventDetail.occurrenceAddress", source = "occurrenceAddress",
            qualifiedBy = LocationAddressMapper.ToLocationAddress.class)
    @Mapping(target = "eventDetail.occurrenceEndTime", source = "occurrenceEndTime")
    @Mapping(target = "eventDetail.occurrenceStartTime", source = "occurrenceStartTime")
    @Mapping(target = "eventDetail.legalPaper", source = "legalPaper")
    @Mapping(target = "eventDetail.description", source = "description",
            qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Event fromUpdateDtoToModel(UpdateEventDto dto);

    @Named("toEventOpenPositions")
    public Integer toEventOpenPositions(List<EventOpenPosition> eventOpenPositions) {
        return eventOpenPositions.size();
    }
}
