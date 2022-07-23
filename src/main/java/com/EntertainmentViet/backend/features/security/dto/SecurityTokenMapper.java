package com.EntertainmentViet.backend.features.security.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public interface SecurityTokenMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"tokenType", "notBeforePolicy", "sessionState", "scope"})
  LoginTokenDto toLoginTokenDto(SuccessLoginResponseDto successLoginResponseDto);
}
