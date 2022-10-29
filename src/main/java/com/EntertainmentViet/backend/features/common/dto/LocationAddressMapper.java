package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.values.LocationAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper(config = MappingConfig.class)
public class LocationAddressMapper {

  @ToReadDto
  public LocationAddress toReadDto(LocationAddress location) {
    if (location != null) {
      return location;
    }

    return LocationAddress.builder()
        .street("")
        .district("")
        .city("")
        .build();
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToReadDto {
  }
}
