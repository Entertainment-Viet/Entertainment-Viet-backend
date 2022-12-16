package com.EntertainmentViet.backend.features.talent.dto.talent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.admin.dto.TalentFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreMapper;
import com.EntertainmentViet.backend.features.security.roles.TalentRole;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.PackageMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {
        ExtensionsMapper.class,
        UserInputTextMapper.class,
        ReviewMapper.class,
        BookingMapper.class,
        TalentFeedBackMapper.class,
        PackageMapper.class,
        CategoryMapper.class,
        ScoreMapper.class,
        LocationMapper.class
    },
        config = MappingConfig.class)
public abstract class TalentMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "bookings", "reviews", "reviewSum", "finalScore"})
    @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
    @Mapping(target = "bio", source = "talentDetail.bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
    @Mapping(target = "phoneNumber", source = "talentDetail.phoneNumber")
    @Mapping(target = "email", source = "talentDetail.email")
    @Mapping(target = "address", source = "talentDetail.address")
    @Mapping(target = "taxId", source = "talentDetail.taxId")
    @Mapping(target = "bankAccountNumber", source = "talentDetail.bankAccountNumber")
    @Mapping(target = "bankAccountOwner", source = "talentDetail.bankAccountOwner")
    @Mapping(target = "bankName", source = "talentDetail.bankName")
    @Mapping(target = "bankBranchName", source = "talentDetail.bankBranchName")
    @Mapping(target = "fullName", source = "talentDetail.fullName")
    @Mapping(target = "citizenId", source = "talentDetail.citizenId")
    @Mapping(target = "citizenPaper", source = "talentDetail.citizenPaper")
    @Mapping(target = "avatar", source = "talentDetail.avatar")
    @Mapping(target = "descriptionImg", source = "talentDetail.descriptionImg")
    @Mapping(target = "extensions", source = "talentDetail.extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    @Mapping(target = "songs", source = "priorityScores", qualifiedBy = ScoreMapper.FromModelToScoreSongListDto.class)
    @Mapping(target = "rewards", source = "priorityScores", qualifiedBy = ScoreMapper.FromModelToScoreRewardListDto.class)
    @Mapping(target = "avgReviewRate", source = ".", qualifiedByName = "toAvgReviewRate")
    @Mapping(target = "reviewCount", source = ".", qualifiedByName = "toReviewCount")
    @Mapping(target = "accountType", source = "accountType", qualifiedByName = "toAccountTypeKey")
    public abstract ReadTalentDto toDto(Talent talent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "reviewSum", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userState", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    @Mapping(target = "packages", ignore = true)
    @Mapping(target = "finalScore", ignore = true)
    @Mapping(target = "priorityScores", ignore = true)
    @Mapping(target = "talentDetail.extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    @Mapping(target = "talentDetail.bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
    @Mapping(target = "offerCategories", source = "offerCategories", qualifiedByName = "toOfferCategories")
    public abstract Talent fromUpdateDtoToModel(UpdateTalentDto updateTalentDto);

    @BeanMapping(ignoreUnmappedSourceProperties = {"songs", "rewards"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "reviewSum", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userState", ignore = true)
    @Mapping(target = "packages", ignore = true)
    @Mapping(target = "finalScore", ignore = true)
    @Mapping(target = "offerCategories", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "accountType", source = "accountType", qualifiedByName = "toAccountType")
    @Mapping(target = "talentDetail.phoneNumber", source = "phoneNumber")
    @Mapping(target = "talentDetail.address", source = "address")
    @Mapping(target = "talentDetail.taxId", source = "taxId")
    @Mapping(target = "talentDetail.bankAccountNumber", source = "bankAccountNumber")
    @Mapping(target = "talentDetail.bankAccountOwner", source = "bankAccountOwner")
    @Mapping(target = "talentDetail.bankName", source = "bankName")
    @Mapping(target = "talentDetail.bankBranchName", source = "bankBranchName")
    @Mapping(target = "talentDetail.fullName", source = "fullName")
    @Mapping(target = "talentDetail.citizenId", source = "citizenId")
    @Mapping(target = "talentDetail.citizenPaper", source = "citizenPaper")
    @Mapping(target = "priorityScores", source = ".", qualifiedBy = ScoreMapper.FromKycDtoToModel.class)
    public abstract Talent fromKycDtoToModel(UpdateTalentKycInfoDto kycInfoDto);

    @BeanMapping(ignoreUnmappedSourceProperties = {"password"})
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "reviewSum", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userState", ignore = true)
    @Mapping(target = "accountType", ignore = true)
    @Mapping(target = "packages", ignore = true)
    @Mapping(target = "finalScore", constant = "0")
    @Mapping(target = "offerCategories", ignore = true)
    @Mapping(target = "priorityScores", ignore = true)
    @Mapping(target = "displayName", source = "username")
    @Mapping(target = "talentDetail.email", source = "email")
    public abstract Talent fromCreateDtoToModel(CreatedTalentDto createdTalentDto);


    // Only return non-confidential detail if token have enough permission
    public ReadTalentDto checkPermission(ReadTalentDto readTalentDto, boolean isOwnerUser) {
        if (!isOwnerUser) {
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
                .avgReviewRate(readTalentDto.getAvgReviewRate())
                .reviewCount(readTalentDto.getReviewCount())
                .build();
        }
        return readTalentDto;
    }

    @Named("toUserStateKey")
    public String toUserStateKey(UserState userState) {
        return userState != null ? userState.i18nKey : null;
    }

    @Named("toAccountTypeKey")
    public String toAccountTypeKey(AccountType accountType) {
        return accountType != null ? accountType.i18nKey : null;
    }

    @Named("toAccountType")
    public AccountType toAccountType(String i18nKey) {
        return AccountType.ofI18nKey(i18nKey);
    }

    @Named("toUserState")
    public UserState toUserState(String i18nKey) {
        return UserState.ofI18nKey(i18nKey);
    }

    @Named("toOfferCategories")
    public Set<Category> toOfferCategories(List<UUID> offerCategoryUidList) {
        if (offerCategoryUidList == null) {
            return Collections.emptySet();
        }

        return offerCategoryUidList.stream()
                .map(uuid -> categoryMapper.toCategory(uuid))
                .collect(Collectors.toSet());
    }

    @Named("toAvgReviewRate")
    public Double toAvgReviewRate(Talent talent) {
        return talent != null ? talent.computeAvgReviewRate() : null;
    }

    @Named("toReviewCount")
    public Integer toReviewCount(Talent talent) {
        return talent != null ? talent.computeTotalReviewCount() : null;
    }

}
