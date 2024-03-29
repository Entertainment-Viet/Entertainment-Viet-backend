package com.EntertainmentViet.backend.features.booking.dto.category;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.TalentCategory;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Mapper(config = MappingConfig.class)
@RequiredArgsConstructor
public abstract class CategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "parentName", source = "parent", qualifiedByName = "toParentName")
    @Mapping(target = "parentUid", source = "parent", qualifiedByName = "toParentUid")
    public abstract ReadCategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "parent", source = "parentUid", qualifiedByName = "toParentCategory")
    public abstract Category toModel(CreateCategoryDto createCategoryDto);

    public ReadCategoryDto fromTalentCategoryToDto(TalentCategory talentCategory) {
        var category = talentCategory.getCategory();
        return toDto(category);
    }

    @ToCategory
    public Category toCategory(UUID categoryUid) {
        if (categoryUid == null) {
            return null;
        }
        return categoryRepository.findByUid(categoryUid).orElse(null);
    }

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
        if (uid == null) {
            return null;
        }
        return categoryRepository.findByUid(uid).orElse(null);
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface ToCategory {
    }

}
