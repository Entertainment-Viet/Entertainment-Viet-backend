package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

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

    @BeanMapping(ignoreUnmappedSourceProperties = {"jobDetailDto", "createdAt", "organizerUid", "talentUid"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isPaid", source = "paid")
    @Mapping(target = "jobDetail", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "toBookingStatus")
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "talent", ignore = true)
    public abstract Booking toModel(BookingDto bookingDto);

    @Named("toTalentUid")
    public UUID toTalentUid(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }

    @Named("toOrganizerUid")
    public UUID toOrganizerUid(Organizer organizer) {
        return organizer != null ? organizer.getUid() : null;
    }

    @Named("toBookingStatus")
    public BookingStatus toBookingStatus(String status) {
        return status != null ? BookingStatus.ofI18nKey(status) : null;
    }
}
