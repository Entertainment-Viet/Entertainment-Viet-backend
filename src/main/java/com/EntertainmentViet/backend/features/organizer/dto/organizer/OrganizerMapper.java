package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.booking.dto.location.LocationMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.StandardTypeMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.organizer.dto.event.EventMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
    ExtensionsMapper.class,
    JobOfferMapper.class,
    UserInputTextMapper.class,
    EventMapper.class,
    BookingMapper.class,
    LocationMapper.class,
    StandardTypeMapper.class}, config = MappingConfig.class)
public abstract class OrganizerMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = { "id", "shoppingCart", "conversations" })
  @Mapping(target = "userState", source = "userState", qualifiedBy = StandardTypeMapper.ToUserStateKey.class)
  @Mapping(target = "extensions", source = "organizerDetail.extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
  @Mapping(target = "phoneNumber", source = "organizerDetail.phoneNumber")
  @Mapping(target = "email", source = "organizerDetail.email")
  @Mapping(target = "address", source = "organizerDetail.address")
  @Mapping(target = "taxId", source = "organizerDetail.taxId")
  @Mapping(target = "bankAccountNumber", source = "organizerDetail.bankAccountNumber")
  @Mapping(target = "bankAccountOwner", source = "organizerDetail.bankAccountOwner")
  @Mapping(target = "bankName", source = "organizerDetail.bankName")
  @Mapping(target = "bankBranchName", source = "organizerDetail.bankBranchName")
  @Mapping(target = "companyName", source = "organizerDetail.companyName")
  @Mapping(target = "representative", source = "organizerDetail.representative")
  @Mapping(target = "position", source = "organizerDetail.position")
  @Mapping(target = "businessPaper", source = "organizerDetail.businessPaper")
  @Mapping(target = "avatar", source = "organizerDetail.avatar")
  @Mapping(target = "descriptionImg", source = "organizerDetail.descriptionImg")
  @Mapping(target = "bio", source = "organizerDetail.bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
  @Mapping(target = "accountType", source = "accountType", qualifiedBy = StandardTypeMapper.ToAccountTypeKey.class)
  @Mapping(target = "userType", source = "userType", qualifiedBy = StandardTypeMapper.ToUserTypeKey.class)
  public abstract ReadOrganizerDto toDto(Organizer organizer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uid", ignore = true)
  @Mapping(target = "jobOffers", ignore = true)
  @Mapping(target = "events", ignore = true)
  @Mapping(target = "bookings", ignore = true)
  @Mapping(target = "shoppingCart", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "userState", ignore = true)
  @Mapping(target = "userType", ignore = true)
  @Mapping(target = "accountType", ignore = true)
  @Mapping(target = "archived", ignore = true)
  @Mapping(target = "conversations", ignore = true)
  @Mapping(target = "organizerDetail.avatar", source = "avatar")
  @Mapping(target = "organizerDetail.descriptionImg", source = "descriptionImg")
  @Mapping(target = "organizerDetail.extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
  @Mapping(target = "organizerDetail.bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
  public abstract Organizer fromUpdateDtoToModel(UpdateOrganizerDto updateOrganizerDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uid", ignore = true)
  @Mapping(target = "jobOffers", ignore = true)
  @Mapping(target = "events", ignore = true)
  @Mapping(target = "bookings", ignore = true)
  @Mapping(target = "shoppingCart", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "userState", ignore = true)
  @Mapping(target = "displayName", ignore = true)
  @Mapping(target = "archived", ignore = true)
  @Mapping(target = "accountType", ignore = true)
  @Mapping(target = "conversations", ignore = true)
  @Mapping(target = "hashTag", ignore = true)
  @Mapping(target = "userType", source = "userType", qualifiedBy = StandardTypeMapper.ToUserType.class)
  @Mapping(target = "organizerDetail.phoneNumber", source = "phoneNumber")
  @Mapping(target = "organizerDetail.address", source = "address")
  @Mapping(target = "organizerDetail.taxId", source = "taxId")
  @Mapping(target = "organizerDetail.bankAccountNumber", source = "bankAccountNumber")
  @Mapping(target = "organizerDetail.bankAccountOwner", source = "bankAccountOwner")
  @Mapping(target = "organizerDetail.bankName", source = "bankName")
  @Mapping(target = "organizerDetail.bankBranchName", source = "bankBranchName")
  @Mapping(target = "organizerDetail.companyName", source = "companyName")
  @Mapping(target = "organizerDetail.representative", source = "representative")
  @Mapping(target = "organizerDetail.position", source = "position")
  @Mapping(target = "organizerDetail.businessPaper", source = "businessPaper")
  public abstract Organizer fromKycDtoToModel(UpdateOrganizerKycInfoDto kycInfoDto);

  @BeanMapping(ignoreUnmappedSourceProperties = { "password" })
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uid", ignore = true)
  @Mapping(target = "jobOffers", ignore = true)
  @Mapping(target = "events", ignore = true)
  @Mapping(target = "bookings", ignore = true)
  @Mapping(target = "shoppingCart", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "userState", ignore = true)
  @Mapping(target = "accountType", ignore = true)
  @Mapping(target = "userType", ignore = true)
  @Mapping(target = "archived", ignore = true)
  @Mapping(target = "conversations", ignore = true)
  @Mapping(target = "displayName", source = "username")
  @Mapping(target = "organizerDetail.email", source = "email")
  public abstract Organizer fromCreateDtoToModel(CreatedOrganizerDto createdOrganizerDto);

  // Only return non-confidential detail if token have enough permission
  public ReadOrganizerDto checkPermission(ReadOrganizerDto readOrganizerDto, boolean isOwnerUser) {
    if (!isOwnerUser) {
      return ReadOrganizerDto.builder()
          .uid(readOrganizerDto.getUid())
          .events(readOrganizerDto.getEvents())
          .displayName(readOrganizerDto.getDisplayName())
          .phoneNumber(readOrganizerDto.getPhoneNumber())
          .email(readOrganizerDto.getEmail())
          .address(readOrganizerDto.getAddress())
          .bio(readOrganizerDto.getBio())
          .createdAt(readOrganizerDto.getCreatedAt())
          .extensions(readOrganizerDto.getExtensions())
          .avatar(readOrganizerDto.getAvatar())
          .descriptionImg(readOrganizerDto.getDescriptionImg())
          .hashTag(readOrganizerDto.getHashTag())
          .build();
    }
    return readOrganizerDto;
  }
}
