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

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "reviews", "bookings", "feedbacks"})
    @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
    @Mapping(target = "reviews", ignore = true) // TODO: remove and fix this
    @Mapping(target = "bookings", ignore = true) // TODO: remove and fix this
    @Mapping(target = "feedbacks", ignore = true) // TODO: remove and fix this
    @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    public abstract TalentDto toDto(Talent talent);

    @BeanMapping(ignoreUnmappedSourceProperties = {"bio", "reviews", "bookings", "feedbacks"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserState")
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "reviews", ignore = true) // TODO: remove and fix this
    @Mapping(target = "bookings", ignore = true) // TODO: remove and fix this
    @Mapping(target = "feedbacks", ignore = true) // TODO: remove and fix this
    @Mapping(target = "bio", ignore = true) // TODO: remove and fix this
    public abstract Talent toModel(TalentDto talentDto);


    @Named("toUserStateKey")
    public String toUserStateKey(UserState userState) {
        return userState != null ? userState.i18nKey : null;
    }

    @Named("toUserState")
    public UserState toUserState(String i18nKey) {
        return UserState.ofI18nKey(i18nKey);
    }
}
