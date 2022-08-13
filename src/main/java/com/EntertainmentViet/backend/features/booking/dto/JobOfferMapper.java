package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.domain.values.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class JobOfferMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "organizer"})
    @Mapping(target = "jobDetailDto", source = "jobDetail", qualifiedByName = "toJobDetailDto")
    public abstract JobOfferDto toDto(JobOffer jobOffer);

    @Named("toJobDetailDto")
    public JobDetailDto toJobDetailDto(JobDetail jobDetail) {
        if (jobDetail != null) {
            Category category = jobDetail.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category.getName());
            if (category.getParent() != null) {
                categoryDto.setParentName(category.getParent().getName());
            }
            JobDetailDto jobDetailDto = new JobDetailDto();
            jobDetailDto.setCategoryDto(categoryDto);
            return jobDetailDto;
        }
       return null;
    }
}
