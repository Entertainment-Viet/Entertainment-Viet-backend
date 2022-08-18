package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
        ExtensionsMapper.class,
        UserInputTextMapper.class,
        ReviewMapper.class,
        BookingMapper.class,
        TalentFeedBackMapper.class
    },
        config = MappingConfig.class)
public abstract class TalentMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
    @Mapping(target = "reviews", source = "reviews")
    @Mapping(target = "bookings", source = "bookings")
    @Mapping(target = "feedbacks", source = "feedbacks")
    @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    public abstract TalentDto toDto(Talent talent);

    @Named("toUserStateKey")
    public String toUserStateKey(UserState userState) {
        return userState != null ? userState.i18nKey : null;
    }
}
