package com.EntertainmentViet.backend.features.booking.dto.jobdetail;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(uses = {
        CategoryMapper.class,
        PriceMapper.class,
        UserInputTextMapper.class,
        ExtensionsMapper.class,
        LocationMapper.class
    },
    config = MappingConfig.class)
public abstract class JobDetailMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = { "id" })
    @Mapping(target = "workType", source = "workType.i18nKey")
    @Mapping(target = "note", source = "note", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    public abstract ReadJobDetailDto toDto(JobDetail jobDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workType", source = "workType", qualifiedByName = "toWorkType")
    @Mapping(target = "note", source = "note", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "category", source = "categoryId", qualifiedBy = CategoryMapper.ToCategory.class)
    @Mapping(target = "location", source = "locationId", qualifiedBy = LocationMapper.ToLocation.class)
    public abstract JobDetail fromCreateDtoToModel(CreateJobDetailDto jobDetailDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workType", source = "workType", qualifiedByName = "toWorkType")
    @Mapping(target = "note", source = "note", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "category", source = "categoryId", qualifiedBy = CategoryMapper.ToCategory.class)
    @Mapping(target = "location", source = "locationId", qualifiedBy = LocationMapper.ToLocation.class)
    public abstract JobDetail fromUpdateDtoToModel(UpdateJobDetailDto jobDetailDto);

    @Named("toWorkType")
    public WorkType toWorkType(String i18nKey) {
        return WorkType.ofI18nKey(i18nKey);
    }
}
