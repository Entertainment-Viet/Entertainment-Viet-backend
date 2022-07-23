package com.EntertainmentViet.backend.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("keycloak")
@Getter
@Setter
@NoArgsConstructor
public class KeycloakProperties {
  private String realm;
  private String authServerUrl;
  private String resource;
}
