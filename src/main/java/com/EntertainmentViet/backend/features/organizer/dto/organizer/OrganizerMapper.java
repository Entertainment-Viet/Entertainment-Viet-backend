package com.EntertainmentViet.backend.features.organizer.dto.organizer;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.OrganizerFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.booking.BookingMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.organizer.dto.event.EventMapper;
import com.EntertainmentViet.backend.features.organizer.dto.joboffer.JobOfferMapper;
import com.EntertainmentViet.backend.features.security.roles.OrganizerRole;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.*;

@Slf4j
@Mapper(uses = {
    ExtensionsMapper.class,
    JobOfferMapper.class,
    UserInputTextMapper.class,
    EventMapper.class,
    BookingMapper.class,
    OrganizerFeedBackMapper.class
  },
  config = MappingConfig.class)
public abstract class OrganizerMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
  @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
  @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
  @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
  @Mapping(target = "shoppingCart", source = "shoppingCart", qualifiedByName = "toShoppingCartUid")
  public abstract ReadOrganizerDto toDto(Organizer organizer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uid", ignore = true)
  @Mapping(target = "jobOffers", ignore = true)
  @Mapping(target = "events", ignore = true)
  @Mapping(target = "bookings", ignore = true)
  @Mapping(target = "feedbacks", ignore = true)
  @Mapping(target = "shoppingCart", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "userState", ignore = true)
  @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
  @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
  public abstract Organizer toModel(UpdateOrganizerDto updateOrganizerDto);

  // Only return non-confidential detail if token have enough permission
  public ReadOrganizerDto checkPermission(ReadOrganizerDto readOrganizerDto) {
    if (!SecurityUtils.hasRole(OrganizerRole.READ_ORGANIZER_DETAIL.name())) {
      return ReadOrganizerDto.builder()
          .events(readOrganizerDto.getEvents())
          .displayName(readOrganizerDto.getDisplayName())
          .phoneNumber(readOrganizerDto.getPhoneNumber())
          .email(readOrganizerDto.getEmail())
          .address(readOrganizerDto.getAddress())
          .bio(readOrganizerDto.getBio())
          .createdAt(readOrganizerDto.getCreatedAt())
          .extensions(readOrganizerDto.getExtensions())
          .build();
    }
    return readOrganizerDto;
  }

  @Named("toUserStateKey")
  public String toUserStateKey(UserState userState) {
    return userState != null ? userState.i18nKey : null;
  }

  @Named("toUserState")
  public UserState toUserState(String i18nKey) {
    return UserState.ofI18nKey(i18nKey);
  }

  @Named("toShoppingCartUid")
  public List<UUID> toShoppingCartUid(Set<Package> shoppingCart) {
    if (shoppingCart == null) {
      return Collections.emptyList();
    }
    List<UUID> resultList = new ArrayList<>();
    for (Package cartItem : shoppingCart ){
      resultList.add(cartItem.getUid());
    }
    return resultList;
  }

  @Named("toShoppingCart")
  public Set<Package> toShoppingCart(List<UUID> uidList) {
    if (uidList == null) {
      return Collections.emptySet();
    }
    Set<Package> resultList = new HashSet<>();
    for (UUID uid : uidList ){
      resultList.add(Package.builder().build());
    }
    return resultList;
  }
}
