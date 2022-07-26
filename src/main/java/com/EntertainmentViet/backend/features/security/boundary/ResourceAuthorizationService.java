package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.features.booking.api.CategoryController;
import com.EntertainmentViet.backend.features.organizer.api.OrganizerController;
import com.EntertainmentViet.backend.features.security.roles.OrganizerRole;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

@Service
public class ResourceAuthorizationService implements ResourceAuthorizationBoundary<HttpSecurity> {

  @Override
  public void authorizeRequests(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .mvcMatchers(HttpMethod.GET, anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.READ_ORGANIZER.name())
            .mvcMatchers(anyPathAfter(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.READ_ORGANIZER.name())
            .anyRequest().authenticated());
  }

  private String anyPathAfter(String pattern) {
    return pattern + "/**";
  }
}
