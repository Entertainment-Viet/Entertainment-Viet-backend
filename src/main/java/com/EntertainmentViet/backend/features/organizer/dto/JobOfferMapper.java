package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {JobDetailMapper.class}, config = MappingConfig.class)
public abstract class JobOfferMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "organizer"})
    @Mapping(target = "organizerId", ignore = true) // TODO fix this
    public abstract JobOfferDto toDto(JobOffer jobOffer);

    @BeanMapping(ignoreUnmappedSourceProperties = {"organizerId"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizer", ignore = true) // TODO fix this
    public abstract JobOffer toModel(JobOfferDto jobDetailDto);

}
