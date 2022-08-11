package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class CategoryMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "id", source = "uid")
    @Mapping(target = "parentName", source = "parent", qualifiedByName = "toParentName")
    public abstract CategoryDto toDto(Category category);

    @Named("toParentName")
    public String toParentName(Category category) {
        return category != null ? category.getName() : null;
    }

}
