package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Event;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {
        EventOpenPositionMapper.class,
        JobOfferMapper.class,
        EntityMapper.class,
        UserInputTextMapper.class,
        LocationMapper.class,
}, config = MappingConfig.class)
public abstract class EventMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = { "id", "archived" })
    @Mapping(target = "occurrenceAddress", source = "eventDetail.occurrenceAddress")
    @Mapping(target = "occurrenceStartTime", source = "eventDetail.occurrenceStartTime")
    @Mapping(target = "occurrenceEndTime", source = "eventDetail.occurrenceEndTime")
    @Mapping(target = "descriptionImg", source = "eventDetail.descriptionImg")
    @Mapping(target = "legalPaper", source = "eventDetail.legalPaper")
    @Mapping(target = "openPositionsCount", source = "openPositions", qualifiedByName = "toEventOpenPositions")
    @Mapping(target = "description", source = "eventDetail.description", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "organizerId", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerUid.class)
    @Mapping(target = "organizerName", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerName.class)
    @Mapping(target = "organizerAvatar", source = "organizer.organizerDetail.avatar")
    public abstract ReadEventDto toReadDto(Event event);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openPositions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "archived", constant = "false")
    @Mapping(target = "eventDetail.occurrenceAddress", source = "occurrenceAddress")
    @Mapping(target = "eventDetail.occurrenceEndTime", source = "occurrenceEndTime")
    @Mapping(target = "eventDetail.occurrenceStartTime", source = "occurrenceStartTime")
    @Mapping(target = "eventDetail.legalPaper", source = "legalPaper")
    @Mapping(target = "eventDetail.descriptionImg", source = "descriptionImg")
    @Mapping(target = "eventDetail.description", source = "description", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Event fromCreateDtoToModel(CreateEventDto dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openPositions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "eventDetail.occurrenceAddress", source = "occurrenceAddress")
    @Mapping(target = "eventDetail.occurrenceEndTime", source = "occurrenceEndTime")
    @Mapping(target = "eventDetail.occurrenceStartTime", source = "occurrenceStartTime")
    @Mapping(target = "eventDetail.descriptionImg", source = "descriptionImg")
    @Mapping(target = "eventDetail.legalPaper", source = "legalPaper")
    @Mapping(target = "eventDetail.description", source = "description",
            qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    @Mapping(target = "archived", constant = "false")
    public abstract Event fromUpdateDtoToModel(UpdateEventDto dto);

    @Named("toEventOpenPositions")
    public Integer toEventOpenPositions(List<EventOpenPosition> eventOpenPositions) {
        return eventOpenPositions.size();
    }
}
