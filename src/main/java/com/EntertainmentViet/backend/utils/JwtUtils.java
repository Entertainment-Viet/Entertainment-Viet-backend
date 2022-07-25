package com.EntertainmentViet.backend.utils;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Optional;

@UtilityClass
@Slf4j
public class JwtUtils {
  public final String PREFERRED_USERNAME = "preferred_username";

  public Optional<JWTClaimsSet> getPayloadFromToken(String token) {
    if (token == null) {
      return Optional.empty();
    }

    JWTClaimsSet claimSet = null;
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      var jsonPayload = jwt.getPayload().toJSONObject();
      if (jsonPayload == null) {
        log.info("Can not decode the JWT token: " + token);
        return Optional.empty();
      }
      claimSet = JWTClaimsSet.parse(jsonPayload);
    } catch (ParseException e) {
      log.info("Can not get expiration timestamp from JWT token: " + token);
    }
    return Optional.ofNullable(claimSet);
  }
}
