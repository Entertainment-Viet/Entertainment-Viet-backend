package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class RestUtils {

  public <T> Page<T> getPageEntity(List<T> list, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), list.size());
    return new PageImpl<>(list.subList(start, end), pageable, list.size());
  }

  // For lazy loading in Application level only
  public <T> CustomPage<T> toPageResponse(Page<T> page) {
    return new CustomPage<>(page);
  }


  // For lazy loading up to DB level
  public <T> CustomPage<T> toLazyLoadPageResponse(Page<T> page) {
    var lazyLoadCustomPage = new CustomPage<>(page);
    lazyLoadCustomPage.getPaging().setTotalPages(null);
    lazyLoadCustomPage.getPaging().setTotalElements(null);

    return lazyLoadCustomPage;
  }

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
