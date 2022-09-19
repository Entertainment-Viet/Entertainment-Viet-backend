package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.PackageMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
        ExtensionsMapper.class,
        UserInputTextMapper.class,
        ReviewMapper.class,
        BookingMapper.class,
        TalentFeedBackMapper.class,
        PackageMapper.class
    },
        config = MappingConfig.class)
public abstract class TalentMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "bookings"})
    @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
    @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    public abstract ReadTalentDto toDto(Talent talent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userState", ignore = true)
    @Mapping(target = "packages", ignore = true)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    public abstract Talent toModel(UpdateTalentDto updateTalentDto);


    @Named("toUserStateKey")
    public String toUserStateKey(UserState userState) {
        return userState != null ? userState.i18nKey : null;
    }

    @Named("toUserState")
    public UserState toUserState(String i18nKey) {
        return UserState.ofI18nKey(i18nKey);
    }
}
