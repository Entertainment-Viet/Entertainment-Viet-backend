package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.OrganizerFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
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

  @BeanMapping(ignoreUnmappedSourceProperties = {"id"}) // TODO: enable jobOffers source mapping
  @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
  @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
  @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToTranslatedText.class)
  @Mapping(target = "shoppingCart", source = "shoppingCart", qualifiedByName = "toShoppingCartUid")
  public abstract OrganizerDto toDto(Organizer organizer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserState")
  @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
  @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
  @Mapping(target = "shoppingCart", source = "shoppingCart", qualifiedByName = "toShoppingCart")
  public abstract Organizer toModel(OrganizerDto organizerDto);

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
    List<UUID> resultList = new ArrayList<>();
    for (Package cartItem : shoppingCart ){
      resultList.add(cartItem.getUid());
    }
    return resultList;
  }

  @Named("toShoppingCart")
  public Set<Package> toShoppingCart(List<UUID> uidList) {
    // TODO fix this
    Set<Package> resultList = new HashSet<>();
    for (UUID uid : uidList ){
      resultList.add(Package.builder().build());
    }
    return resultList;
  }
}
