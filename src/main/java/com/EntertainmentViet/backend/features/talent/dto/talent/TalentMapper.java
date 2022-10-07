package com.EntertainmentViet.backend.features.talent.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.security.roles.TalentRole;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.PackageMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(uses = {
        ExtensionsMapper.class,
        UserInputTextMapper.class,
        ReviewMapper.class,
        BookingMapper.class,
        TalentFeedBackMapper.class,
        PackageMapper.class,
        CategoryMapper.class
    },
        config = MappingConfig.class)
public abstract class TalentMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "bookings", "scoreSystem", "reviews"})
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
    @Mapping(target = "finalScore", ignore = true)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "scoreSystem", source = "scoreSystem", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    @Mapping(target = "offerCategories", source = "offerCategories", qualifiedByName = "toOfferCategories")
    public abstract Talent toModel(UpdateTalentDto updateTalentDto);

    // Only return non-confidential detail if token have enough permission
    public ReadTalentDto checkPermission(ReadTalentDto readTalentDto) {
        if (!SecurityUtils.hasRole(TalentRole.READ_TALENT_DETAIL.name())) {
            return ReadTalentDto.builder()
                .uid(readTalentDto.getUid())
                .packages(readTalentDto.getPackages())
                .offerCategories(readTalentDto.getOfferCategories())
                .displayName(readTalentDto.getDisplayName())
                .phoneNumber(readTalentDto.getPhoneNumber())
                .email(readTalentDto.getEmail())
                .address(readTalentDto.getAddress())
                .bio(readTalentDto.getBio())
                .createdAt(readTalentDto.getCreatedAt())
                .extensions(readTalentDto.getExtensions())
                .build();
        }
        return readTalentDto;
    }

    @Named("toUserStateKey")
    public String toUserStateKey(UserState userState) {
        return userState != null ? userState.i18nKey : null;
    }

    @Named("toUserState")
    public UserState toUserState(String i18nKey) {
        return UserState.ofI18nKey(i18nKey);
    }

    @Named("toOfferCategories")
    public Set<Category> toOfferCategories(List<UUID> offerCategoryUidList) {
        return offerCategoryUidList.stream()
                .map(uuid -> categoryMapper.toCategory(uuid))
                .collect(Collectors.toSet());
    }
}
