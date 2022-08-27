package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class CategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "parentName", source = "parent", qualifiedByName = "toParentName")
    public abstract CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", source = "parentName", qualifiedByName = "toParentCategory")
    public abstract Category toModel(CategoryDto categoryDto);

    @Named("toParentName")
    public String toParentName(Category category) {
        return category != null ? category.getName() : null;
    }

    @Named("toParentCategory")
    public Category toParentName(String parentName) {
        return categoryRepository.findByName(parentName).orElse(null);
    }
}
