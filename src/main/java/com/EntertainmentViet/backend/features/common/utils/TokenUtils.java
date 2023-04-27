package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.text.ParseException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class TokenUtils {

  private ObjectMapper objectMapper = new ObjectMapper();

  public UUID getUid(JwtAuthenticationToken token) {
    String rawUid = token.getToken().getClaim("sub");
    return UUID.fromString(rawUid);
  }

  public boolean isTokenContainPermissions(JwtAuthenticationToken token, String... permissions) {
    var permissionList = token.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    var missingPermissionsCount = Arrays.stream(permissions)
        .filter(permission -> !permissionList.contains(permission))
        .count();
    return missingPermissionsCount == 0;
  }

  public String getRedirectUrl(String rawToken) {
    try {
      var rawPayload = JWSObject.parse(rawToken).getPayload().toString();
      var payloadJson = objectMapper.readTree(rawPayload);
      var redirectUrl = payloadJson.get("reduri");
      return redirectUrl.toString().replace("\"", "");
    } catch (JsonProcessingException | ParseException e) {
      throw new InvalidInputException("Can not getting redirectUrl from token: " + rawToken);
    }
  }

  public UUID getUid(String rawToken) {
    try {
      var rawPayload = JWSObject.parse(rawToken).getPayload().toString();
      var payloadJson = objectMapper.readTree(rawPayload);
      var redirectUrl = payloadJson.get("sub");
      return UUID.fromString(redirectUrl.toString().replace("\"", ""));
    } catch (JsonProcessingException | ParseException e) {
      throw new InvalidInputException("Can not getting redirectUrl from token: " + rawToken);
    }
  }
}
