package com.EntertainmentViet.backend.features.organizer.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class OrganizerMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"id", "jobOffers", "events", "bookings", "feedbacks"}) // TODO: enable jobOffers source mapping
  @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserStateKey")
  @Mapping(target = "jobOfferDto", ignore = true) // TODO: remove and fix this
  public abstract OrganizerDto toDto(Organizer organizer);

  @BeanMapping(ignoreUnmappedSourceProperties = {"jobOfferDto"}) // TODO: remove and fix this
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userState", source = "userState", qualifiedByName = "toUserState")
  @Mapping(target = "jobOffers", ignore = true) // TODO: remove and fix this
  @Mapping(target = "events", ignore = true) // TODO: remove and fix this
  @Mapping(target = "bookings", ignore = true) // TODO: remove and fix this
  @Mapping(target = "feedbacks", ignore = true) // TODO: remove and fix this
  public abstract Organizer toModel(OrganizerDto organizerDto);

  @Named("toUserStateKey")
  public String toUserStateKey(UserState userState) {
    return userState != null ? userState.i18nKey : null;
  }

  @Named("toUserState")
  public UserState toUserState(String i18nKey) {
    return UserState.ofI18nKey(i18nKey);
  }
}
