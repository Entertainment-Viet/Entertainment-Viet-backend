package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
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
        ExtensionsMapper.class
    },
    config = MappingConfig.class)
public abstract class JobDetailMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "workType", source = "workType.i18nKey")
    @Mapping(target = "note", source = "note", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    public abstract JobDetailDto toDto(JobDetail jobDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workType", source = "workType", qualifiedByName = "toWorkType")
    @Mapping(target = "note", source = "note", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    public abstract JobDetail toModel(JobDetailDto jobDetailDto);

    @Named("toWorkType")
    public WorkType toWorkType(String i18nKey) {
        return WorkType.ofI18nKey(i18nKey);
    }
}
