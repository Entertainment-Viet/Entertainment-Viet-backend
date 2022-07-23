package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.config.properties.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

  private final KeycloakProperties keycloakProperties;

  @Bean
  public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

//  @Bean
//  public KeycloakDeployment keycloakDeployment(AdapterConfig adapterConfig) {
//    return KeycloakDeploymentBuilder.build(adapterConfig);
//  }

  @Bean
  public WebClient keycloakApiClient() {
    return WebClient.create(String.format("%s/realms/%s",
        keycloakProperties.getAuthServerUrl(), keycloakProperties.getRealm()));
  }

}
