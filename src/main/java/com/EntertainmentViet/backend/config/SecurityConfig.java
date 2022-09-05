package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.features.security.boundary.ResourceAuthorizationBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String CLIENT_ID = "backend";

  private final ResourceAuthorizationBoundary<HttpSecurity> resourceAuthorizationBoundary;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    resourceAuthorizationBoundary.authorizeRequests(http);
    http.cors(Customizer.withDefaults())
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter(CLIENT_ID));

    resourceAuthorizationBoundary.ignoreCsrfPaths(http.csrf());
    http.csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    return http.build();
  }

  private class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final String resourceId;

    public CustomJwtAuthenticationConverter(String resourceId) {
      this.resourceId = resourceId;
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
      Collection<GrantedAuthority> authorities = Stream.concat(defaultGrantedAuthoritiesConverter.convert(source)
                  .stream(),
              extractResourceRoles(source, resourceId).stream())
          .collect(Collectors.toSet());
      return new JwtAuthenticationToken(source, authorities);
    }

    private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt, final String resourceId) {
      Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      Map<String, Object> resource;
      Collection<String> resourceRoles;
      if (resourceAccess != null && (resource = (Map<String, Object>) resourceAccess.get(resourceId)) != null &&
          (resourceRoles = (Collection<String>) resource.get("roles")) != null)
        return resourceRoles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
      return Collections.emptySet();
    }
  }
}
