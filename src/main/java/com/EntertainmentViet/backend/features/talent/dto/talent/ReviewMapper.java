package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class)
public abstract class ReviewMapper {

    // TODO
    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "createdAt", "talent", "comment", "score"})
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "score", ignore = true)
    public abstract ReviewDto toDto(Review review);

    // TODO
    @BeanMapping(ignoreUnmappedSourceProperties = {"createdAt", "talent", "comment", "score"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "score", ignore = true)
    public abstract Review toModel(ReviewDto dto);
}
