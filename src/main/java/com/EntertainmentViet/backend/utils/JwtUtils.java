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
 public Optional<JWTClaimsSet> getPayloadFromToken(String token) {
   if (token == null) {
     return Optional.empty();
   }

   JWTClaimsSet claimSet = null;
   try {
     SignedJWT jwt = SignedJWT.parse(token);
     claimSet = JWTClaimsSet.parse(jwt.getPayload().toJSONObject());
   } catch (ParseException e) {
     log.error("Can not get expiration timestamp from JWT token", e);
   }
   return Optional.ofNullable(claimSet);
 }
}
