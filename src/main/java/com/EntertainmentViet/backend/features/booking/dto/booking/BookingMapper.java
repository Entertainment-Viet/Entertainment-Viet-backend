package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.organizer.dao.organizer.OrganizerRepository;
import com.EntertainmentViet.backend.features.talent.dao.TalentRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(uses = {
        JobDetailMapper.class,
}, config = MappingConfig.class)
public abstract class BookingMapper {

    @Autowired
    private JobDetailMapper jobDetailMapper;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private TalentRepository talentRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "isPaid", source = "paid")
    @Mapping(target = "status", source = "status.i18nKey")
    @Mapping(target = "organizerUid", source = "organizer", qualifiedByName = "toOrganizerUid")
    @Mapping(target = "talentUid", source = "talent", qualifiedByName = "toTalentUid")
    public abstract ReadBookingDto toReadDto(Booking booking);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isPaid", constant = "false")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "organizer", source = "organizerUid", qualifiedByName = "toOrganizerEntity")
    @Mapping(target = "talent", source = "talentUid", qualifiedByName = "toTalentEntity")
    public abstract Booking fromCreateDtoToModel(CreateBookingDto createBookingDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "talent", ignore = true)
    public abstract Booking fromUpdateDtoToModel(UpdateBookingDto updateBookingDto);

    //TODO REMOVE THIS
    @BeanMapping(ignoreUnmappedSourceProperties = {"jobDetail", "createdAt", "organizerUid", "talentUid"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isPaid", source = "paid")
    @Mapping(target = "jobDetail", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "toBookingStatus")
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "talent", ignore = true)
    public abstract Booking toModel(ReadBookingDto bookingDto);

    @Named("toTalentUid")
    public UUID toTalentUid(Talent talent) {
        return talent != null ? talent.getUid() : null;
    }

    @Named("toTalentEntity")
    public Talent toTalentEntity(UUID talentUid) {
        return talentRepository.findByUid(talentUid).orElse(null);
    }

    @Named("toOrganizerUid")
    public UUID toOrganizerUid(Organizer organizer) {
        return organizer != null ? organizer.getUid() : null;
    }

    @Named("toOrganizerEntity")
    public Organizer toOrganizerEntity(UUID organizerUid) {
        return organizerRepository.findByUid(organizerUid).orElse(null);
    }

    @Named("toBookingStatus")
    public BookingStatus toBookingStatus(String status) {
        return status != null ? BookingStatus.ofI18nKey(status) : null;
    }
}
