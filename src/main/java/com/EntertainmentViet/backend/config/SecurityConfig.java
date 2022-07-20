package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.features.security.AuthorizationBoundary;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@RequiredArgsConstructor
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  private final AuthorizationBoundary<HttpSecurity> authorizationBoundary;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
    grantedAuthorityMapper.setConvertToUpperCase(true);

    KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
    auth.authenticationProvider(keycloakAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    authorizationBoundary.authorizeRequests(http);
  }

  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }
}
