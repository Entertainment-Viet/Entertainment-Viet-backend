package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.config.properties.AuthenticationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

  private final AuthenticationProperties authenticationProperties;

  public static Integer TIMEOUT = 5000;

  @Bean
  public RestTemplate keycloakRestTemplate() {
    String baseKeycloakUrl = String.format("%s", authenticationProperties.getKeycloak().getServerUrl());

    return new RestTemplateBuilder()
        .requestFactory(this::getClientHttpRequestFactory)
        .uriTemplateHandler(new DefaultUriBuilderFactory(baseKeycloakUrl))
        .build();
  }

  @Bean
  public ClientHttpRequestFactory getClientHttpRequestFactory() {
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setReadTimeout(TIMEOUT);
    factory.setConnectTimeout(TIMEOUT);
    factory.setConnectionRequestTimeout(TIMEOUT);
    factory.setHttpClient(httpClient());
    factory.setBufferRequestBody(false);

    return factory;
  }

  @Bean
  public CloseableHttpClient httpClient() {
    return HttpClients.createSystem();
  }
}
