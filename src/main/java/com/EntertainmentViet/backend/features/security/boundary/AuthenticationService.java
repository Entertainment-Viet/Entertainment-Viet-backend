package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.config.properties.KeycloakProperties;
import com.EntertainmentViet.backend.config.properties.SecretProperties;
import com.EntertainmentViet.backend.features.security.dto.LoginInfoDto;
import com.EntertainmentViet.backend.features.security.dto.LoginTokenDto;
import com.EntertainmentViet.backend.features.security.dto.SecurityTokenMapper;
import com.EntertainmentViet.backend.features.security.dto.SuccessLoginResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements AuthenticationBoundary {

  private final WebClient keycloakTokenApiClient;

  private final SecurityTokenMapper securityTokenMapper;

  private final KeycloakProperties keycloakProperties;

  private final SecretProperties secretProperties;

  @Override
  public LoginTokenDto login(Duration timeout, LoginInfoDto loginInfo) {
    var loginResponse = keycloakTokenApiClient.post()
        .uri("/protocol/openid-connect/token")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData("grant_type", "password")
            .with("username", loginInfo.getUsername())
            .with("password", loginInfo.getPassword())
            .with("client_id", keycloakProperties.getResource())
            .with("client_secret", secretProperties.getClientSecret())
        )
        .retrieve()
        .bodyToMono(SuccessLoginResponseDto.class)
        .onErrorMap(e -> {
          log.error("Get error when getting token from keycloak", e);
          return e;
        })
        .block(timeout);

    return securityTokenMapper.toLoginTokenDto(loginResponse);
  }

  @Override
  public void logout(HttpServletRequest request) {
    keycloakSessionLogout(request);
    servletSessionLogout(request);
  }


  private void servletSessionLogout(HttpServletRequest request) {
    try {
      request.logout();
    } catch (ServletException e) {
      log.error("Can not logout from keycloak.",e);
    }
  }

  private void keycloakSessionLogout(HttpServletRequest request){
    RefreshableKeycloakSecurityContext context = getKeycloakSecurityContext(request);
    KeycloakDeployment keycloakDeployment = context.getDeployment();
    context.logout(keycloakDeployment);
  }

  private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request){
    return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
  }
}
