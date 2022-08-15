package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dto.CategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {JobDetailMapper.class}, config = MappingConfig.class)
public abstract class JobOfferMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "organizer"})
    @Mapping(target = "jobDetailDto", source = "jobDetail")
    public abstract JobOfferDto toDto(JobOffer jobOffer);

}
