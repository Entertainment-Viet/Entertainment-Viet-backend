package com.EntertainmentViet.backend.features.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class RestUtils {

  public URI getCreatedLocationUri(HttpServletRequest request, UUID id) {
    return ServletUriComponentsBuilder
        .fromRequestUri(request)
        .path("/{id}")
        .buildAndExpand(id.toString())
        .toUri();
  }

  public UUID getUidFromToken(JwtAuthenticationToken token) {
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
}
