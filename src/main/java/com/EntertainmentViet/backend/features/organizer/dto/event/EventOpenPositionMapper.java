package com.EntertainmentViet.backend.features.organizer.dto.event;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.common.dto.StandardTypeMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
    JobOfferMapper.class,
    EntityMapper.class,
    StandardTypeMapper.class
},
    config = MappingConfig.class)
public abstract class EventOpenPositionMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "applicants", "archived"})
    @Mapping(target = "eventId", source = "event", qualifiedBy = EntityMapper.ToEventUid.class)
    @Mapping(target = "applicantCount", source = ".", qualifiedByName = "toApplicantCount")
    @Mapping(target = "paymentType", source = "paymentType", qualifiedBy = StandardTypeMapper.ToPaymentTypeKey.class)
    public abstract ReadEventOpenPositionDto toDto(EventOpenPosition eventOpenPosition);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicants", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "paymentType", source = "paymentType", qualifiedBy = StandardTypeMapper.ToPaymentType.class)
    @Mapping(target = "archived", constant = "false")
    public abstract EventOpenPosition fromCreateDtoToModel(CreateEventOpenPositionDto dto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicants", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "paymentType", source = "paymentType", qualifiedBy = StandardTypeMapper.ToPaymentType.class)
    @Mapping(target = "archived", constant = "false")
    public abstract EventOpenPosition fromUpdateDtoToModel(UpdateEventOpenPositionDto dto);

    @Named("toApplicantCount")
    public Integer toApplicantCount(EventOpenPosition eventOpenPosition) {
        return eventOpenPosition.getApplicants().size();
    }
}
