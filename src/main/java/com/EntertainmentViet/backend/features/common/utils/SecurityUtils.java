package com.EntertainmentViet.backend.features.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class SecurityUtils {

  public Set<String> getRoles() {
    return SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  public boolean hasRole(String role) {
    return getRoles().contains(role);
  }

}
