package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.Shoppable;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.features.admin.dto.OrganizerFeedBackMapper;
import com.EntertainmentViet.backend.features.booking.dto.BookingMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.ShoppableDto;
import com.EntertainmentViet.backend.features.common.dto.UserInputTextMapper;
import com.EntertainmentViet.backend.features.talent.dto.PackageDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  @Mapping(target = "shoppables", source = "shoppables", qualifiedByName = "toShoppablesDto")
  public abstract OrganizerDto toDto(Organizer organizer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserState")
  @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
  @Mapping(target = "bio", source = "bio", qualifiedBy = UserInputTextMapper.ToUserInputTextObject.class)
  @Mapping(target = "shoppables", source = "shoppables", qualifiedByName = "toShoppables")
  public abstract Organizer toModel(OrganizerDto organizerDto);

  @Named("toUserStateKey")
  public String toUserStateKey(UserState userState) {
    return userState != null ? userState.i18nKey : null;
  }

  @Named("toUserState")
  public UserState toUserState(String i18nKey) {
    return UserState.ofI18nKey(i18nKey);
  }

  @Named("toShoppablesDto")
  public List<ShoppableDto> toShoppablesDto(List<Shoppable> shoppables) {
    // TODO fix this
    List<ShoppableDto> resultList = new ArrayList<>();
    for (Shoppable shoppable : shoppables ){
      if (shoppable instanceof Package) {
        resultList.add(PackageDto.builder().build());
      } else {
        log.warn("Can not convert shoppable item in organizer entity to organizerDto ");
      }
    }
    return resultList;
  }

  @Named("toShoppables")
  public List<Shoppable> toShoppables(List<ShoppableDto> dtos) {
    // TODO fix this
    List<Shoppable> resultList = new ArrayList<>();
    for (ShoppableDto dto : dtos ){
      if (dto instanceof Package) {
        resultList.add(Package.builder().build());
      } else {
        log.warn("Can not convert shoppable dto in organizerDto");
      }
    }
    return resultList;
  }
}
