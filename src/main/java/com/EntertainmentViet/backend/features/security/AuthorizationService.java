package com.EntertainmentViet.backend.features.security;

import com.EntertainmentViet.backend.features.organizer.api.OrganizerController;
import com.EntertainmentViet.backend.features.security.roles.OrganizerRole;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements AuthorizationBoundary<HttpSecurity> {

  @Override
  public void authorizeRequests(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(HttpMethod.GET, anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
        .hasAuthority(OrganizerRole.READ_ORGANIZER.name())
        .anyRequest().permitAll();
  }

  private String anyPathAfter(String pattern) {
    return pattern + "/**";
  }
}
