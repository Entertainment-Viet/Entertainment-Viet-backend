package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
        JobDetailMapper.class,
}, config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class BookingMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "isPaid", source = "paid")
    @Mapping(target = "status", source = "status.i18nKey")
    @Mapping(target = "jobDetailDto", source = "jobDetail")
    @Mapping(target = "organizerUid", source = "organizer", qualifiedByName = "toOrganizerUid")
    @Mapping(target = "talentUid", source = "talent", qualifiedByName = "toTalentUid")
    public abstract BookingDto toDto(Booking booking);

    @Named("toTalentUid")
    public String toTalentUid(Talent talent) {
        return talent != null ? talent.getUid().toString() : null;
    }

    @Named("toOrganizerUid")
    public String toOrganizerUid(Organizer organizer) {
        return organizer != null ? organizer.getUid().toString() : null;
    }
}
