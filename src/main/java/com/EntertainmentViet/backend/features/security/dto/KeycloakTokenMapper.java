package com.EntertainmentViet.backend.features.security.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.utils.JwtUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.Date;

@Mapper(config = MappingConfig.class)
@Slf4j
public abstract class KeycloakTokenMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"tokenType", "notBeforePolicy", "sessionState", "scope", "expiresIn", "refreshExpiresIn"})
  @Mapping(source = "accessToken", target = "accessExpiresIn", qualifiedByName = "toExpiredTime")
  @Mapping(source = "refreshToken", target = "refreshExpiresIn", qualifiedByName = "toExpiredTime")
  public abstract LoginResponseDto toLoginResponseDto(KeycloakTokenDto keycloakTokenDto);

  @Named("toExpiredTime")
  protected Instant toExpiredTime(String token) {
    return JwtUtils.getPayloadFromToken(token)
        .map(JWTClaimsSet::getExpirationTime)
        .map(Date::toInstant)
        .orElse(null);
  }
}
