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

import java.util.UUID;

@Mapper(config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class CategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "parentName", source = "parent", qualifiedByName = "toParentName")
    @Mapping(target = "parentUid", source = "parent", qualifiedByName = "toParentUid")
    public abstract CategoryDto toDto(Category category);

    @BeanMapping(ignoreUnmappedSourceProperties = {"parentName"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", source = "parentUid", qualifiedByName = "toParentCategory")
    public abstract Category toModel(CategoryDto categoryDto);

    @Named("toParentName")
    public String toParentName(Category category) {
        return category != null ? category.getName() : null;
    }

    @Named("toParentUid")
    public UUID toParentUid(Category category) {
        return category != null ? category.getUid() : null;
    }

    @Named("toParentCategory")
    public Category toParentCategory(UUID uid) {
        return categoryRepository.findByUid(uid).orElse(null);
    }
}
