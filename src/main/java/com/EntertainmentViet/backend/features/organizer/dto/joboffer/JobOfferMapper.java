package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    JobDetailMapper.class,
    EntityMapper.class
}, config = MappingConfig.class)
public abstract class JobOfferMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "archived"})
    @Mapping(target = "organizerId", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerUid.class)
    @Mapping(target = "organizerName", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerName.class)
    public abstract ReadJobOfferDto toDto(JobOffer jobOffer);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "archived", ignore = true)
    public abstract JobOffer fromCreateDtoToModel(CreateJobOfferDto createJobOfferDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "archived", ignore = true)
    public abstract JobOffer fromUpdateDtoToModel(UpdateJobOfferDto updateJobOfferDto);
}
