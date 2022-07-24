package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.config.properties.KeycloakProperties;
import com.EntertainmentViet.backend.config.properties.SecretProperties;
import com.EntertainmentViet.backend.features.security.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements AuthenticationBoundary {

  private final WebClient keycloakTokenApiClient;

  private final KeycloakTokenMapper keycloakTokenMapper;

  private final KeycloakProperties keycloakProperties;

  private final SecretProperties secretProperties;

  @Override
  public Optional<LoginResponseDto> login(Duration timeout, LoginRequestDto request) {
    var loginResponse = keycloakTokenApiClient.post()
        .uri("/protocol/openid-connect/token")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData("grant_type", "password")
            .with("username", request.getUsername())
            .with("password", request.getPassword())
            .with("client_id", keycloakProperties.getResource())
            .with("client_secret", secretProperties.getClientSecret())
            .with("scope", "openid")
        )
        .retrieve()
        .onStatus(HttpStatus.UNAUTHORIZED::equals,
            response -> response.bodyToMono(String.class).map(Exception::new))
        .bodyToMono(KeycloakLoginTokenDto.class)
        .onErrorMap(e -> {
          log.error("Get error when getting token from keycloak", e);
          return e;
        })
        .onErrorReturn(new KeycloakLoginTokenDto())
        .block(timeout);

    if (loginResponse.getAccessToken() == null) {
      return Optional.empty();
    }
    return Optional.of(keycloakTokenMapper.toLoginResponseDto(loginResponse));
  }

  @Override
  public void logout(Duration timeout, LogoutRequestDto request) {
    keycloakTokenApiClient.post()
        .uri("/protocol/openid-connect/logout")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData("refresh_token", request.getRefreshToken())
            .with("client_id", keycloakProperties.getResource())
            .with("client_secret", secretProperties.getClientSecret())
        )
        .retrieve()
        .bodyToMono(Void.class)
        .onErrorMap(e -> {
          log.error("Get error when revoking token in keycloak", e);
          return e;
        })
        .block(timeout);

//    keycloakSessionLogout(request);
//    servletSessionLogout(request);
  }


//  private void servletSessionLogout(HttpServletRequest request) {
//    try {
//      request.logout();
//    } catch (ServletException e) {
//      log.error("Can not logout from keycloak.",e);
//    }
//  }
//
//  private void keycloakSessionLogout(HttpServletRequest request){
//    RefreshableKeycloakSecurityContext context = getKeycloakSecurityContext(request);
//    KeycloakDeployment keycloakDeployment = context.getDeployment();
//    context.logout(keycloakDeployment);
//  }
//
//  private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request){
//    return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
//  }
}
