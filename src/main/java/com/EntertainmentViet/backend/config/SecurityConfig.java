package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.config.properties.SecurityProperties;
import com.EntertainmentViet.backend.features.security.boundary.ResourceAuthorizationBoundary;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
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

  private final SecurityProperties securityProperties;

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

  @Bean
  public CustomJwtAuthenticationConverter jwtAuthenticationConverter() {
    return new CustomJwtAuthenticationConverter(CLIENT_ID);
  }

  public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(securityProperties.getAllowedOrigins());
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
    configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
