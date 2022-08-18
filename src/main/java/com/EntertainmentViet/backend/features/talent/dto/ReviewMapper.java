package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class ReviewMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "createdAt", "talent", "comment", "score"})
    public abstract ReviewDto toDto(Review review);
}
