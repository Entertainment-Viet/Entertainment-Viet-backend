package com.EntertainmentViet.backend.features.admin.dto.talent;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.StandardTypeMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.scoresystem.dto.ScoreMapper;
import com.EntertainmentViet.backend.features.talent.dto.packagetalent.PackageMapper;
import com.EntertainmentViet.backend.features.talent.dto.talent.ReviewMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(uses = {
    ExtensionsMapper.class,
    UserInputTextMapper.class,
    ReviewMapper.class,
    BookingMapper.class,
    PackageMapper.class,
    CategoryMapper.class,
    ScoreMapper.class,
    LocationMapper.class,
    StandardTypeMapper.class,
},
    config = MappingConfig.class)
public abstract class AdminTalentMapper {

  @Autowired
  private CategoryMapper categoryMapper;

  @BeanMapping(ignoreUnmappedSourceProperties = {"id", "bookings", "reviews","reviewSum", "finalScore", "conversations"})
  @Mapping(target = "userState", source = "userState", qualifiedBy = StandardTypeMapper.ToUserStateKey.class)
  @Mapping(target = "userType", source = "userType", qualifiedBy = StandardTypeMapper.ToUserTypeKey.class)
  @Mapping(target = "bio", source = "talentDetail.bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
  @Mapping(target = "phoneNumber", source = "talentDetail.phoneNumber")
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
  public abstract ReadAdminTalentDto toAdminDto(Talent talent);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uid", ignore = true)
  @Mapping(target = "reviews", ignore = true)
  @Mapping(target = "reviewSum", ignore = true)
  @Mapping(target = "bookings", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "userState", ignore = true)
  @Mapping(target = "packages", ignore = true)
  @Mapping(target = "finalScore", ignore = true)
  @Mapping(target = "accountType", ignore = true)
  @Mapping(target = "userType", ignore = true)
  @Mapping(target = "archived", ignore = true)
  @Mapping(target = "conversations", ignore = true)
  @Mapping(target = "displayName", ignore = true)
  @Mapping(target = "talentDetail", ignore = true)
  @Mapping(target = "offerCategories", ignore = true)
  @Mapping(target = "email", ignore = true)
  public abstract Talent toModel(UpdateAdminTalentDto updateAdminTalentDto);

  @Named("toOfferCategories")
  public Set<Category> toOfferCategories(List<UUID> offerCategoryUidList) {
    if (offerCategoryUidList == null) {
      return Collections.emptySet();
    }
    return offerCategoryUidList.stream()
        .map(uuid -> categoryMapper.toCategory(uuid))
        .collect(Collectors.toSet());
  }
}
