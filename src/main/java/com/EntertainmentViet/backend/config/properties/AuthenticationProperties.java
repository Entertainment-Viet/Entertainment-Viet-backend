package com.EntertainmentViet.backend.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("authentication")
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationProperties {

  private KeycloakSsoProp keycloak;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class KeycloakSsoProp {
    private String realm;
    private String serverUrl;
    private String resource;
    private String adminUsername;
    private String adminPassword;
  }
}
