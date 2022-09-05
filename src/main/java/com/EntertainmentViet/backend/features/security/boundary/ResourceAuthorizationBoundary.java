package com.EntertainmentViet.backend.features.security.boundary;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;

public interface ResourceAuthorizationBoundary<T> {
  void authorizeRequests(T http) throws Exception;

  void ignoreCsrfPaths(CsrfConfigurer<HttpSecurity> csrfConfigurer);
}
